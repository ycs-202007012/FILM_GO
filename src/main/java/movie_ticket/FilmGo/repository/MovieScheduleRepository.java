package movie_ticket.FilmGo.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import movie_ticket.FilmGo.domain.movie.Movie;
import movie_ticket.FilmGo.domain.theater.*;
import movie_ticket.FilmGo.domain.theater.enums.MovieScheduleStatus;
import movie_ticket.FilmGo.repository.search.TheaterSearch;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static movie_ticket.FilmGo.domain.theater.QMovieSchedule.movieSchedule;

@Repository
@RequiredArgsConstructor
public class MovieScheduleRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public MovieSchedule save(MovieSchedule movieSchedule) {
        em.persist(movieSchedule);
        return movieSchedule;
    }

    /*public Optional<MovieSchedule> findByTheaterHouseAndTime(TheaterSearch theaterSearch, LocalDateTime time) {
        return em.createQuery("SELECT ms FROM MovieSchedule ms WHERE ms.theaterHouse.theater.", MovieSchedule.class)
                .setParameter("theaterHouseId", theaterHouseId)
                .setParameter("startTime", startTime)
                .setParameter("theaterId", theaterId)
                .getResultStream().findFirst();
    }*/

    public List<MovieSchedule> findByTheaterHouse(TheaterSearch theaterSearch) {
        return query.select(movieSchedule)
                .from(movieSchedule)
                .where(likeName(theaterSearch.getName()), movieSchedule.status.eq(MovieScheduleStatus.REGISTERED))
                .fetch();
    }

    public List<MovieSchedule> findSchedulesByMovieAndTheater(Movie movie, Theater theater) {
        return query.select(movieSchedule)
                .from(movieSchedule)
                .where(movieSchedule.movie.eq(movie), movieSchedule.theaterHouse.theater.eq(theater))
                .fetch();
    }

    public MovieSchedule findById(Long id) {
        return em.find(MovieSchedule.class, id);
    }

    public void removeSchedule(MovieSchedule movieSchedule) {
        em.remove(movieSchedule);
    }

    private BooleanExpression likeName(String name) {
        if (StringUtils.hasText(name)) {
            return movieSchedule.movie.title.like("%" + name + "%");
        }
        return null;
    }
}
