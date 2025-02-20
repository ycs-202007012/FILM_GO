package movie_ticket.FilmGo.controller.booking;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.controller.booking.dto.BookingRequestForm;
import movie_ticket.FilmGo.domain.movie.Movie;
import movie_ticket.FilmGo.domain.movie.enums.MovieStatus;
import movie_ticket.FilmGo.domain.theater.MovieSchedule;
import movie_ticket.FilmGo.domain.theater.Seat;
import movie_ticket.FilmGo.domain.theater.Theater;
import movie_ticket.FilmGo.domain.theater.TheaterHouse;
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

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class BookingController {

    private final SeatService seatService;
    private final TheaterMovieService theaterMovieService;
    private final MovieService movieService;
    private final TheaterService theaterService;
    private final MovieScheduleService movieScheduleService;

    @GetMapping("")
    public String booking(@RequestParam(required = false) Long movieId,
                          @RequestParam(required = false) Long theaterId,
                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate,
                          Model model) {

        // 1️⃣ 현재 상영 중인 영화 목록 조회
        List<Movie> movies = movieService.getActiveMovies();
        model.addAttribute("movies", movies);

        // 2️⃣ 기본 영화 및 극장 선택
        movieId = movieService.getDefaultMovieId(movieId, movies);
        theaterId = theaterService.getDefaultTheaterId(movieId, theaterId);

        List<TheaterMovie> theaterMovieList = theaterMovieService.findAllTheaterMovieByMovie(movieService.findById(movieId).get());
        // 3️⃣ 선택된 영화 & 극장의 스케줄 조회
        List<MovieSchedule> movieSchedules = movieScheduleService.getMovieSchedules(movieId, theaterId);

        // 4️⃣ 날짜별 그룹화 및 선택된 날짜 필터링
        Map<LocalDate, List<MovieSchedule>> movieScheduleMap = movieScheduleService.groupSchedulesByDate(movieSchedules);
        selectedDate = movieScheduleService.getDefaultSelectedDate(selectedDate, movieScheduleMap);
        List<MovieSchedule> selectedSchedules = movieScheduleMap.getOrDefault(selectedDate, Collections.emptyList());

        // 5️⃣ 모델에 데이터 전달
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
        model.addAttribute("schedule", schedule);
        model.addAttribute("movie", schedule.getMovie());
        model.addAttribute("theater", schedule.getTheaterHouse().getTheater());
        model.addAttribute("theaterHouse", schedule.getTheaterHouse());

        return "ticket/movie-booking";
    }

    @PostMapping("/booking/{movieScheduleId}")
    public String bookingMovie(@Validated @ModelAttribute(name = "form") BookingRequestForm form, BindingResult bindingResult,
                               @PathVariable Long movieScheduleId) {

        if (bindingResult.hasErrors()) {
            return "/booking/" + movieScheduleId;
        }

        MovieSchedule schedule = movieScheduleService.findById(movieScheduleId);
        List<Integer> seatIds = form.getSeatIds();
        List<Seat> selectedSeats = schedule.getTheaterHouse().getSeats().stream().filter(seat -> seatIds.contains(seat.getId()))
                .toList();

        for (Seat selectedSeat : selectedSeats) {
            selectedSeat.updateStatus(SeatStatus.PENDING);
        }

        return "redirect:/";
    }


}
