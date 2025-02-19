package movie_ticket.FilmGo.controller.booking;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.controller.booking.dto.BookingRequestForm;
import movie_ticket.FilmGo.domain.movie.Movie;
import movie_ticket.FilmGo.domain.movie.enums.MovieStatus;
import movie_ticket.FilmGo.domain.theater.MovieSchedule;
import movie_ticket.FilmGo.domain.theater.Theater;
import movie_ticket.FilmGo.domain.theater.TheaterHouse;
import movie_ticket.FilmGo.domain.theater.enums.TheaterStatus;
import movie_ticket.FilmGo.domain.thmv.TheaterMovie;
import movie_ticket.FilmGo.repository.search.MovieSearch;
import movie_ticket.FilmGo.repository.search.TheaterSearch;
import movie_ticket.FilmGo.service.MovieScheduleService;
import movie_ticket.FilmGo.service.MovieService;
import movie_ticket.FilmGo.service.TheaterMovieService;
import movie_ticket.FilmGo.service.TheaterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class BookingController {

    private final MovieService movieService;
    private final TheaterService theaterService;
    private final MovieScheduleService movieScheduleService;
    private final TheaterMovieService theaterMovieService;

    @GetMapping("")
    public String booking(@RequestParam(required = false) Long movieId,
                          @RequestParam(required = false) Long theaterId,
                          Model model) {
        // 1️⃣ 현재 상영 중인 모든 영화 조회 (기본 표시)
        List<Movie> movies = movieService.findAll(new MovieSearch(null, MovieStatus.ACTIVE));
        model.addAttribute("movies", movies);

        // 2️⃣ 기본 선택된 영화 설정 (없으면 첫 번째 영화 선택)
        if (movieId == null && !movies.isEmpty()) {
            movieId = movies.get(0).getId();
        }

        // 3️⃣ 해당 영화가 상영 중인 극장 목록 조회 (기본 표시)
        List<TheaterMovie> theaterMovieList = theaterMovieService.findAllTheaterMovieByMovie(
                movieService.findById(movieId)
                        .orElseThrow(() -> new RuntimeException("해당 ID로 조회가 되지 않습니다."))
        );
        model.addAttribute("theaterMovieList", theaterMovieList);

        // 4️⃣ 기본 선택된 극장 설정 (없으면 첫 번째 극장 선택)
        if (theaterId == null && !theaterMovieList.isEmpty()) {
            theaterId = theaterMovieList.get(0).getTheater().getId();
        }

        // 5️⃣ 선택된 극장과 영화의 스케줄 조회
        Theater theater = theaterService.findById(theaterId);
        Movie movie = movieService.findById(movieId).get();
        List<MovieSchedule> movieSchedules = movieScheduleService.findSchedulesByMovieAndTheater(movie, theater);

        for (MovieSchedule movieSchedule : movieSchedules) {
            movieSchedule.getTheaterHouse().getSeats().size();
        }

        // 6️⃣ 날짜별 그룹화
        Map<LocalDate, List<MovieSchedule>> movieScheduleMap = movieSchedules.stream()
                .collect(Collectors.groupingBy(schedule -> schedule.getTime().getStartTime().toLocalDate(),
                        LinkedHashMap::new, Collectors.toList()));



        model.addAttribute("movieScheduleMap", movieScheduleMap);
        model.addAttribute("selectedMovieId", movieId);
        model.addAttribute("selectedTheaterId", theaterId);

        return "ticket/movie";
    }


}
