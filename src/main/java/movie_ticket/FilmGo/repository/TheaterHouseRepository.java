package movie_ticket.FilmGo.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import movie_ticket.FilmGo.domain.theater.Theater;
import movie_ticket.FilmGo.domain.theater.TheaterHouse;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TheaterHouseRepository {

    private final EntityManager em;

    public TheaterHouse save(TheaterHouse theaterHouse) {
        em.persist(theaterHouse);
        return theaterHouse;
    }

    public TheaterHouse findById(Long id) {
        return em.find(TheaterHouse.class, id);
    }

    public Optional<TheaterHouse> findByNumber(String name, Long theaterId) {
        return em.createQuery("select t from TheaterHouse t where t.houseName = :name and t.theater.id = :theaterId", TheaterHouse.class)
                .setParameter("name", name)
                .setParameter("theaterId", theaterId) // theater 객체의 ID를 전달
                .getResultList()
                .stream()
                .findFirst();
    }

    public List<TheaterHouse> findAll(Theater theater) {
        return em.createQuery("select t from TheaterHouse t where t.theater = :theater", TheaterHouse.class)
                .setParameter("theater", theater)
                .getResultList();
    }

    public void delete(TheaterHouse theaterHouse) {
        em.remove(theaterHouse);
    }
}
