package movie_ticket.FilmGo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import movie_ticket.FilmGo.controller.login.LoginSession;
import movie_ticket.FilmGo.domain.member.Member;
import movie_ticket.FilmGo.domain.member.enums.MemberRole;
import movie_ticket.FilmGo.domain.movie.Movie;
import movie_ticket.FilmGo.domain.movie.enums.MovieStatus;
import movie_ticket.FilmGo.domain.thmv.TheaterMovie;
import movie_ticket.FilmGo.repository.search.MovieSearch;
import movie_ticket.FilmGo.service.MemberService;
import movie_ticket.FilmGo.service.MovieService;
import movie_ticket.FilmGo.service.TheaterMovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MovieService movieService;
    private final TheaterMovieService theaterMovieService;

    @GetMapping("/")
    public String findMovieList(@RequestParam(required = false) String title, Model model, HttpServletRequest request) {
        List<Movie> movies = movieService.findAll(new MovieSearch(title, MovieStatus.ACTIVE));
        model.addAttribute("movies", movies);

        // 상영관 목록 조회
        if (title != null && !title.isEmpty()) {
            MovieSearch movieSearch = new MovieSearch(title, MovieStatus.ACTIVE);
            List<TheaterMovie> theaterMovieList = theaterMovieService.findAllTheaterMovieByMovieSearch(movieSearch);
            model.addAttribute("theaterMovieList", theaterMovieList);
            model.addAttribute("selectedMovie", movieSearch.getTitle());
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            model.addAttribute("memberRole", (MemberRole) session.getAttribute("userRole"));
        }

        return "home";
    }
}
