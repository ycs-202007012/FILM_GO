package movie_ticket.FilmGo.service;

import lombok.RequiredArgsConstructor;
import movie_ticket.FilmGo.domain.theater.MovieSeat;
import movie_ticket.FilmGo.domain.theater.Seat;
import movie_ticket.FilmGo.domain.theater.TheaterHouse;
import movie_ticket.FilmGo.domain.theater.enums.SeatStatus;
import movie_ticket.FilmGo.repository.SeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

    public Seat findById(Long id){
        return seatRepository.findById(id);
    }

}
