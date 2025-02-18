package movie_ticket.FilmGo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.domain.theater.MovieSchedule;
import movie_ticket.FilmGo.domain.theater.StartEndTime;
import movie_ticket.FilmGo.domain.theater.TheaterHouse;
import movie_ticket.FilmGo.repository.MovieScheduleRepository;
import movie_ticket.FilmGo.repository.search.TheaterSearch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MovieScheduleService {

    private final MovieScheduleRepository repository;

    @Transactional
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
            LocalDateTime startA = movieSchedule.getTime().getStartTime();
            LocalDateTime endA = movieSchedule.getTime().getEndTime();
            LocalDateTime startB = startEndTime.getStartTime();
            LocalDateTime endB = startEndTime.getEndTime();

            // 시간이 겹치는 경우 체크
            if (!(endB.isBefore(startA) || startB.isAfter(endA))) {
                return true;
            }
        }
        return false;
    }


}
