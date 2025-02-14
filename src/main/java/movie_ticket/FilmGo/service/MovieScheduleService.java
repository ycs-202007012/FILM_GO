package movie_ticket.FilmGo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.domain.theater.MovieSchedule;
import movie_ticket.FilmGo.domain.theater.StartEndTime;
import movie_ticket.FilmGo.domain.theater.TheaterHouse;
import movie_ticket.FilmGo.repository.MovieScheduleRepository;
import movie_ticket.FilmGo.repository.search.TheaterSearch;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieScheduleService {

    private final MovieScheduleRepository repository;

    public void save(MovieSchedule movieSchedule) {
        repository.save(movieSchedule);
    }

    public MovieSchedule findById(Long id) {
        return repository.findById(id);
    }

    public List<MovieSchedule> findByTheaterSearch(TheaterSearch theaterSearch) {
        return repository.findByTheaterHouse(theaterSearch);
    }

    public boolean checkScheduleTime(TheaterHouse theaterHouse, StartEndTime startEndTime) {
        List<MovieSchedule> movieSchedules = theaterHouse.getMovieSchedules();
        for (MovieSchedule movieSchedule : movieSchedules) {
            if (startEndTime.getStartTime().isBefore(movieSchedule.getTime().getEndTime()) &&
                    startEndTime.getEndTime().isAfter(movieSchedule.getTime().getStartTime())) {
                return true;
            }
        }
        return false;
    }

}
