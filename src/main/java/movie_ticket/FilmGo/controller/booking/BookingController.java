package movie_ticket.FilmGo.controller.booking;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.controller.booking.dto.BookingRequestForm;
import movie_ticket.FilmGo.controller.booking.dto.PaymentRequestForm;
import movie_ticket.FilmGo.domain.member.Member;
import movie_ticket.FilmGo.domain.movie.Movie;
import movie_ticket.FilmGo.domain.movie.enums.MovieStatus;
import movie_ticket.FilmGo.domain.reservation.Reservation;
import movie_ticket.FilmGo.domain.theater.*;
import movie_ticket.FilmGo.domain.theater.enums.SeatStatus;
import movie_ticket.FilmGo.domain.theater.enums.TheaterStatus;
import movie_ticket.FilmGo.domain.thmv.TheaterMovie;
import movie_ticket.FilmGo.repository.search.MovieSearch;
import movie_ticket.FilmGo.repository.search.TheaterSearch;
import movie_ticket.FilmGo.service.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class BookingController {

    private final MovieSeatService seatService;
    private final TheaterMovieService theaterMovieService;
    private final MovieService movieService;
    private final TheaterService theaterService;
    private final MovieScheduleService movieScheduleService;
    private final MemberService memberService;
    private final MovieSeatService movieSeatService;
    private final ReservationService reservationService;

    @GetMapping("")
    public String booking(@RequestParam(defaultValue = "1") Long movieId,
                          @RequestParam(defaultValue = "1") Long theaterId,
                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate,
                          Model model) {

        // 현재 상영 중인 영화 목록 조회
        List<Movie> movies = movieService.getActiveMovies();
        model.addAttribute("movies", movies);

        // 기본 영화 및 극장 선택
        movieId = movieService.getDefaultMovieId(movieId, movies);
        theaterId = theaterService.getDefaultTheaterId(movieId, theaterId);

        List<TheaterMovie> theaterMovieList = theaterMovieService.findAllTheaterMovieByMovie(movieService.findById(movieId).get());
        // 선택된 영화 & 극장의 스케줄 조회
        List<MovieSchedule> movieSchedules = movieScheduleService.getMovieSchedules(movieId, theaterId);

        // 날짜별 그룹화 및 선택된 날짜 필터링
        Map<LocalDate, List<MovieSchedule>> movieScheduleMap = movieScheduleService.groupSchedulesByDate(movieSchedules);
        selectedDate = movieScheduleService.getDefaultSelectedDate(selectedDate, movieScheduleMap);
        List<MovieSchedule> selectedSchedules = movieScheduleService.findByMovieScheduleMap(movieScheduleMap, selectedDate);

        model.addAttribute("theaterMovieList", theaterMovieList);
        model.addAttribute("movieScheduleMap", movieScheduleMap);
        model.addAttribute("selectedSchedules", selectedSchedules);
        model.addAttribute("selectedMovieId", movieId);
        model.addAttribute("selectedTheaterId", theaterId);
        model.addAttribute("selectedDate", selectedDate);

        return "ticket/movie";
    }

    @GetMapping("/booking/{movieScheduleId}")
    public String bookingForm(@ModelAttribute(name = "form") BookingRequestForm form, @PathVariable Long movieScheduleId, Model model) {
        MovieSchedule schedule = movieScheduleService.findById(movieScheduleId);

        form.setMovieId(schedule.getMovie().getId());

        log.info("영화 스케쥴={}", schedule);
        extracted(model, schedule);

        return "ticket/movie-booking";
    }


    @PostMapping("/booking/{movieScheduleId}")
    public String bookingMovie(@Validated @ModelAttribute(name = "form") BookingRequestForm form, BindingResult bindingResult,
                               @PathVariable Long movieScheduleId, Model model, RedirectAttributes redirectAttributes,
                               HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            System.out.println("에러: " + bindingResult.getAllErrors());
            extracted(model, movieScheduleService.findById(movieScheduleId));
            return "ticket/movie-booking";
        }

        MovieSchedule schedule = movieScheduleService.findById(movieScheduleId);
        seatService.reservePendingSeats(form, schedule, request);

        return "redirect:/tickets/booking/" + movieScheduleId + "/payment";
    }

    @GetMapping("/booking/{movieScheduleId}/payment")
    public String paymentForm(@PathVariable Long movieScheduleId, @ModelAttribute(name = "form") PaymentRequestForm form,
                              Model model, HttpServletRequest request) {
        MovieSchedule schedule = movieScheduleService.findById(movieScheduleId);
        Member member = memberService.findByServletRequest(request);

        List<Long> seatIds = new ArrayList<>();
        for (MovieSeat movieSeat : movieSeatService.findSeatsByScheduleAndMember(movieScheduleId, member.getId())) {
            seatIds.add(movieSeat.getId());
        }

        form.setSeatIds(seatIds);
        form.setMemberId(member.getId());
        form.setTotalPrice(movieSeatService.findSeatsByScheduleAndMember(movieScheduleId, member.getId()).size()
                * schedule.getMovie().getPrice());

        extracted(model, schedule, member, form.getSeatIds());
        return "ticket/movie-payment";
    }

    @PostMapping("/booking/{movieScheduleId}/payment")
    public String payment(@PathVariable Long movieScheduleId, @ModelAttribute(name = "form") PaymentRequestForm form,
                          BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            MovieSchedule schedule = movieScheduleService.findById(movieScheduleId);
            Member member = memberService.findById(form.getMemberId());

            // 결제 페이지로 다시 데이터 전송
            extracted(model, schedule, member, form.getSeatIds());
            return "ticket/movie-payment";
        }

        System.out.println("member id:" + form.getMemberId());
        Member member = memberService.findById(form.getMemberId());

        if (form.getChargeMoney() != null) {
            member.chargeMoney(form.getChargeMoney());
        }

        // 금액 부족 시 처리
        if (member.getMoney() < form.getTotalPrice()) {
            bindingResult.reject("noMoney", "결제 금액이 부족합니다");

            MovieSchedule schedule = movieScheduleService.findById(movieScheduleId);
            extracted(model, schedule, member, form.getSeatIds());

            return "ticket/movie-payment";
        }

        // 결제 완료 및 좌석 예약 확정
        MovieSchedule schedule = movieScheduleService.findById(movieScheduleId);
        movieSeatService.confirmReservation(form, schedule);
        Reservation reservation = reservationService.save(Reservation.createReservation(member, schedule.getMovie(), form.getTotalPrice()));
        member.getReservations().add(reservation);

        return "redirect:/tickets/booking/{movieScheduleId}/confirmation";
    }

    @GetMapping("/booking/{movieScheduleId}/confirmation")
    public String paymentConfirmation(@PathVariable Long movieScheduleId, Model model, HttpServletRequest request) {
        MovieSchedule schedule = movieScheduleService.findById(movieScheduleId);
        Member member = memberService.findByServletRequest(request); // 로그인된 회원 정보 가져오기

        List<MovieSeat> reservedSeats = movieSeatService.findSeatsByScheduleAndMember(movieScheduleId, member.getId());
        List<Integer> seatNumbers = reservedSeats.stream()
                .map(seat -> seat.getSeat().getSeatNumber())
                .toList();

        int totalPrice = reservedSeats.size() * schedule.getMovie().getPrice();

        // 모델에 필요한 데이터 추가
        model.addAttribute("schedule", schedule);
        model.addAttribute("member", member);
        model.addAttribute("seatNumbers", seatNumbers);
        model.addAttribute("totalPrice", totalPrice);

        return "ticket/payment-success";
    }


    private static void extracted(Model model, MovieSchedule schedule, Member member, List<Long> seatIds) {
        model.addAttribute("schedule", schedule);
        model.addAttribute("member", member);
        model.addAttribute("seatIds", seatIds);
    }


    private static void extracted(Model model, MovieSchedule schedule) {
        model.addAttribute("schedule", schedule);
        model.addAttribute("movie", schedule.getMovie());
        model.addAttribute("theater", schedule.getTheaterHouse().getTheater());
        model.addAttribute("theaterHouse", schedule.getTheaterHouse());
    }


}
