package movie_ticket.FilmGo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.domain.movie.Movie;
import movie_ticket.FilmGo.domain.theater.MovieSchedule;
import movie_ticket.FilmGo.domain.theater.StartEndTime;
import movie_ticket.FilmGo.domain.theater.Theater;
import movie_ticket.FilmGo.domain.theater.TheaterHouse;
import movie_ticket.FilmGo.repository.MovieRepository;
import movie_ticket.FilmGo.repository.MovieScheduleRepository;
import movie_ticket.FilmGo.repository.TheaterRepository;
import movie_ticket.FilmGo.repository.search.TheaterSearch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MovieScheduleService {

    private final MovieScheduleRepository repository;
    private final TheaterRepository theaterRepository;
    private final MovieScheduleRepository movieScheduleRepository;
    private final MovieRepository movieRepository;

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

    @Transactional
    public void removeSchedule(MovieSchedule schedule) {
        repository.removeSchedule(schedule);
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


    public List<MovieSchedule> findSchedulesByMovieAndTheater(Movie movie, Theater theater) {
        return repository.findSchedulesByMovieAndTheater(movie, theater);
    }

    public List<MovieSchedule> getMovieSchedules(Long movieId, Long theaterId) {
        Theater theater = theaterRepository.findById(theaterId);
        Movie movie = movieRepository.findById(movieId).get();
        List<MovieSchedule> movieSchedules = movieScheduleRepository.findSchedulesByMovieAndTheater(movie, theater);

        // Lazy Loading 방지
        movieSchedules.forEach(schedule -> schedule.getTheaterHouse().getSeats().size());

        return movieSchedules;
    }

    // 날짜별 그룹화
    public Map<LocalDate, List<MovieSchedule>> groupSchedulesByDate(List<MovieSchedule> movieSchedules) {
        return movieSchedules.stream()
                .collect(Collectors.groupingBy(schedule -> schedule.getTime().getStartTime().toLocalDate(),
                        LinkedHashMap::new, Collectors.toList()));
    }

    // 기본 선택된 날짜 가져오기
    public LocalDate getDefaultSelectedDate(LocalDate selectedDate, Map<LocalDate, List<MovieSchedule>> movieScheduleMap) {
        if (selectedDate == null && !movieScheduleMap.isEmpty()) {
            return movieScheduleMap.keySet().iterator().next();
        }
        return selectedDate;
    }
}
