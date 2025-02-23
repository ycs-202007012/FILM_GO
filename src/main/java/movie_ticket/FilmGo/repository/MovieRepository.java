package movie_ticket.FilmGo.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import movie_ticket.FilmGo.controller.movie.dto.MovieForm;
import movie_ticket.FilmGo.domain.movie.Movie;
import movie_ticket.FilmGo.domain.movie.enums.MovieStatus;
import movie_ticket.FilmGo.repository.search.MovieSearch;
import movie_ticket.FilmGo.repository.search.TheaterSearch;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static movie_ticket.FilmGo.domain.movie.QMovie.movie;
import static movie_ticket.FilmGo.domain.theater.QTheater.theater;


@Repository
@RequiredArgsConstructor
public class MovieRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public Movie save(Movie movie) {
        em.persist(movie);
        return movie;
    }

    public Optional<Movie> findById(Long id) {
        return Optional.ofNullable(em.find(Movie.class, id));
    }

    public List<Movie> findAllByTitle(String title) {
        return query.select(movie)
                .from(movie)
                .where(likeName(title), movie.status.eq(MovieStatus.ACTIVE))
                .fetch();

    }

    public List<Movie> findAll(MovieSearch movieSearch) {
        return query.select(movie)
                .from(movie)
                .where(likeName(movieSearch.getTitle()),
                        movieSearch.getStatus() != null ? movie.status.eq(movieSearch.getStatus()) : null)
                .fetch();
    }

    private BooleanExpression likeName(String title) {
        if (StringUtils.hasText(title)) {
            return movie.title.like("%" + title + "%");
        }
        return null;
    }

    public List<Movie> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList(); // ID 리스트가 비어있으면 빈 리스트 반환
        }

        return query.select(movie)
                .from(movie)
                .where(movie.id.in(ids)) // ID 리스트에 포함된 데이터만 필터링
                .fetch();
    }


}
