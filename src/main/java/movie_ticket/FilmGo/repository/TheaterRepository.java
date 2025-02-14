package movie_ticket.FilmGo.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import movie_ticket.FilmGo.controller.theater.dto.TheaterForm;
import movie_ticket.FilmGo.domain.theater.Theater;
import movie_ticket.FilmGo.domain.theater.enums.TheaterStatus;
import movie_ticket.FilmGo.repository.search.TheaterSearch;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static movie_ticket.FilmGo.domain.movie.QMovie.movie;
import static movie_ticket.FilmGo.domain.theater.QTheater.theater;

@Repository
@RequiredArgsConstructor
public class TheaterRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public Theater save(Theater theater) {
        em.persist(theater);
        return theater;
    }

    public Theater findById(Long id) {
        return em.find(Theater.class, id);
    }

    public Optional<Theater> findByName(String name) {
        return em.createQuery("select t from Theater t where t.name = :name", Theater.class)
                .setParameter("name", name)
                .getResultList().stream().findFirst();
    }

    public List<Theater> findAll(TheaterSearch theaterSearch) {
        return query.select(theater)
                .from(theater)
                .where(likeName(theaterSearch.getName()), theater.status.eq(TheaterStatus.REGISTERED))
                .fetch();
    }

    private BooleanExpression likeName(String name) {
        if (StringUtils.hasText(name)) {
            return movie.title.like("%" + name + "%");
        }
        return null;
    }
}
