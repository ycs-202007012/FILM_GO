package movie_ticket.FilmGo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import movie_ticket.FilmGo.domain.reservation.Reservation;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReservationRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public Reservation save(Reservation reservation) {
        em.persist(reservation);
        return reservation;
    }
}
