package movie_ticket.FilmGo.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import movie_ticket.FilmGo.controller.booking.dto.BookingRequestForm;
import movie_ticket.FilmGo.controller.booking.dto.PaymentRequestForm;
import movie_ticket.FilmGo.domain.member.Member;
import movie_ticket.FilmGo.domain.theater.MovieSchedule;
import movie_ticket.FilmGo.domain.theater.MovieSeat;
import movie_ticket.FilmGo.domain.theater.enums.SeatStatus;
import movie_ticket.FilmGo.repository.MovieSeatRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static movie_ticket.FilmGo.controller.login.LoginSession.LOG_ID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MovieSeatService {

    private final MemberService memberService;
    private final MovieSeatRepository movieSeatRepository;

    @Transactional
    public void reservePendingSeats(BookingRequestForm form, MovieSchedule schedule, HttpServletRequest request) {
        List<Long> seatIds = form.getSeatIds();

        // 선택한 좌석 필터링
        List<MovieSeat> selectedSeats = schedule.getMovieSeats().stream()
                .filter(seat -> seatIds.contains(seat.getSeat().getId()))
                .toList();

        HttpSession session = request.getSession(false);
        Long memberId = (Long) session.getAttribute(LOG_ID);
        Member member = memberService.findById(memberId);

        if (member == null) {
            String kakaoId = (String) session.getAttribute("KAKAO_ID");
            member = memberService.findByKakaoId(kakaoId).get();
        }

        for (MovieSeat selectedSeat : selectedSeats) {
            selectedSeat.updateStatus(SeatStatus.PENDING, member);
            selectedSeat.setExpiryTime(5);
        }
    }

    @Transactional
    public void confirmReservation(PaymentRequestForm form, MovieSchedule schedule) {
        List<Long> seatIds = form.getSeatIds();
        List<MovieSeat> selectedSeats = schedule.getMovieSeats().stream()
                .filter(seat -> seatIds.contains(seat.getSeat().getId()))
                .toList();

        Member member = memberService.findById(form.getMemberId());
        member.minusMoney(form.getTotalPrice());

        for (MovieSeat selectedSeat : selectedSeats) {
            selectedSeat.setStatus(SeatStatus.RESERVED);
        }

    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void releaseExpiredReservations() {
        LocalDateTime now = LocalDateTime.now();
        List<MovieSeat> expiredSeats = movieSeatRepository.findAllByStatusAndReservationExpiryTimeBefore(SeatStatus.PENDING, now);

        for (MovieSeat seat : expiredSeats) {
            seat.updateStatus(SeatStatus.AVAILABLE, null); // 좌석 상태 복구
        }
    }

    public List<MovieSeat> findSeatsByScheduleAndMember(Long movieScheduleId, Long id) {
        return movieSeatRepository.findSeatsByScheduleAndMember(movieScheduleId, id);
    }
}
