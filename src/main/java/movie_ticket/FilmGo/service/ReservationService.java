package movie_ticket.FilmGo.service;

import lombok.RequiredArgsConstructor;
import movie_ticket.FilmGo.domain.reservation.Reservation;
import movie_ticket.FilmGo.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository repository;

    @Transactional
    public Reservation save(Reservation reservation) {
        return repository.save(reservation);
    }
}
