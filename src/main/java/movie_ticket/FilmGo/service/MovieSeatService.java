package movie_ticket.FilmGo.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import movie_ticket.FilmGo.controller.booking.dto.BookingRequestForm;
import movie_ticket.FilmGo.controller.login.LoginSession;
import movie_ticket.FilmGo.domain.member.Member;
import movie_ticket.FilmGo.domain.movie.Movie;
import movie_ticket.FilmGo.domain.theater.MovieSchedule;
import movie_ticket.FilmGo.domain.theater.MovieSeat;
import movie_ticket.FilmGo.domain.theater.enums.SeatStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;
import java.util.Optional;

import static movie_ticket.FilmGo.controller.login.LoginSession.LOG_ID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MovieSeatService {

    private final MemberService memberService;

    @Transactional
    public void reservePendingSeats(BookingRequestForm form, MovieSchedule schedule, HttpServletRequest request) {
        List<Long> seatIds = form.getSeatIds();

        // 선택한 좌석 필터링
        List<MovieSeat> selectedSeats = schedule.getMovieSeats().stream()
                .filter(seat -> seatIds.contains(seat.getSeat().getId()))
                .toList();

        HttpSession session = request.getSession(false);
        Long memberId = (Long) session.getAttribute(LOG_ID);
        Optional<Member> member = memberService.findById(memberId);

        if (member.isEmpty()) {
            String kakaoId = (String) session.getAttribute("KAKAO_ID");
            member = memberService.findByKakaoId(kakaoId);
        }

        for (MovieSeat selectedSeat : selectedSeats) {
            selectedSeat.updateStatus(SeatStatus.PENDING, member.get());
        }
    }
}
