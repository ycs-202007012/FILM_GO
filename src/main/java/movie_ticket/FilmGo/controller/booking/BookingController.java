package movie_ticket.FilmGo.controller.booking;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.controller.booking.dto.BookingRequestForm;
import movie_ticket.FilmGo.domain.member.enums.MemberRole;
import movie_ticket.FilmGo.domain.movie.Movie;
import movie_ticket.FilmGo.domain.movie.enums.MovieStatus;
import movie_ticket.FilmGo.domain.theater.Theater;
import movie_ticket.FilmGo.domain.thmv.TheaterMovie;
import movie_ticket.FilmGo.repository.search.MovieSearch;
import movie_ticket.FilmGo.repository.search.TheaterSearch;
import movie_ticket.FilmGo.service.MovieScheduleService;
import movie_ticket.FilmGo.service.MovieService;
import movie_ticket.FilmGo.service.TheaterMovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class BookingController {

    private final MovieService movieService;
    private final MovieScheduleService movieScheduleService;
    private final TheaterMovieService theaterMovieService;

    @GetMapping("")
    public String booking(BookingRequestForm form, Model model, HttpServletRequest request) {
        List<Movie> movies = movieService.findAll(new MovieSearch(null, MovieStatus.ACTIVE));
        model.addAttribute("movies", movies);

        log.info("TITLE == {}", form.getTitle());
        // 상영관 목록 조회
        if (form.getTitle() != null && !form.getTitle().isEmpty()) {
            MovieSearch movieSearch = new MovieSearch(form.getTitle(), MovieStatus.ACTIVE);
            List<TheaterMovie> theaterMovieList = theaterMovieService.findAllTheaterMovieByMovieSearch(movieSearch);

            log.info("THEATER_MOVIE_LIST == {}", theaterMovieList);
            model.addAttribute("theaterMovieList", theaterMovieList);
            model.addAttribute("selectedMovie", movieSearch.getTitle());
        }

        /*if (houseName != null && !houseName.isEmpty()){
            TheaterSearch theaterSearch = new TheaterSearch()
            Theater
        }*/

        return "ticket/movie";
    }

}
