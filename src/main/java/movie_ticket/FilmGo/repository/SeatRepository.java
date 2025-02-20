package movie_ticket.FilmGo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import movie_ticket.FilmGo.domain.theater.Seat;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SeatRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public Seat findById(Long id) {
        return em.find(Seat.class, id);
    }

}
