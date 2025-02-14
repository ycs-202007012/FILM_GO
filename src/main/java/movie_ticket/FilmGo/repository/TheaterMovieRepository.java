package movie_ticket.FilmGo.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import movie_ticket.FilmGo.domain.movie.Movie;
import movie_ticket.FilmGo.domain.movie.enums.MovieStatus;
import movie_ticket.FilmGo.domain.theater.Theater;
import movie_ticket.FilmGo.domain.thmv.QTheaterMovie;
import movie_ticket.FilmGo.domain.thmv.TheaterMovie;
import movie_ticket.FilmGo.domain.thmv.TheaterMovieStatus;
import movie_ticket.FilmGo.repository.search.MovieSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static movie_ticket.FilmGo.domain.movie.QMovie.movie;
import static movie_ticket.FilmGo.domain.thmv.QTheaterMovie.theaterMovie;

@Repository
@RequiredArgsConstructor
public class TheaterMovieRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public TheaterMovie save(TheaterMovie theaterMovie) {
        em.persist(theaterMovie);
        return theaterMovie;
    }

    public Optional<TheaterMovie> findByTheaterAndMovie(Theater theater, Movie movie) {
        return em.createQuery("select tm from TheaterMovie tm where tm.theater = :theater and tm.movie = :movie and tm.status = :status", TheaterMovie.class)
                .setParameter("theater", theater)
                .setParameter("movie", movie)
                .setParameter("status", TheaterMovieStatus.POSSIBLE)
                .getResultList().stream().findFirst();
    }

    public List<TheaterMovie> findAllByTheater(Theater theater) {
        return em.createQuery("select tm from TheaterMovie tm where tm.theater = :theater and tm.status = :status", TheaterMovie.class)
                .setParameter("theater", theater)
                .setParameter("status", TheaterMovieStatus.POSSIBLE)
                .getResultList();
    }

    public List<TheaterMovie> findAllTheaterMovieByMovieSearch(MovieSearch movieSearch) {
        return query.select(theaterMovie)
                .from(theaterMovie)
                .join(theaterMovie.movie, movie)
                .where(movieLikeName(movieSearch.getTitle()),movie.status.eq(MovieStatus.ACTIVE))
                .fetch();
    }

    private BooleanExpression movieLikeName(String title) {
        if (StringUtils.hasText(title)) {
            return movie.title.like("%" + title + "%");
        }
        return null;
    }

}
