package movie_ticket.FilmGo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import movie_ticket.FilmGo.domain.theater.MovieSeat;
import movie_ticket.FilmGo.domain.theater.QMovieSeat;
import movie_ticket.FilmGo.domain.theater.enums.SeatStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static movie_ticket.FilmGo.domain.theater.QMovieSeat.movieSeat;

@Repository
@RequiredArgsConstructor
public class MovieSeatRepository {

    private final JPAQueryFactory query;
    private final EntityManager em;

    public List<MovieSeat> findAllByStatusAndReservationExpiryTimeBefore(SeatStatus status, LocalDateTime time) {
        return query.select(movieSeat)
                .from(movieSeat)
                .where(movieSeat.status.eq(status), movieSeat.reservationExpiryTime.before(time))
                .fetch();
    }

    public List<MovieSeat> findSeatsByScheduleAndMember(Long scheduleId, Long memberId) {
        return query.selectFrom(movieSeat)
                .where(
                        movieSeat.movieSchedule.id.eq(scheduleId),  // 스케줄 ID 일치
                        movieSeat.member.id.eq(memberId),
                        movieSeat.status.eq(SeatStatus.PENDING)// 회원 ID 일치
                )
                .fetch();
    }
}
