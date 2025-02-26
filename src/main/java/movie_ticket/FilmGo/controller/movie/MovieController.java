package movie_ticket.FilmGo.controller.movie;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.domain.member.enums.MemberRole;
import movie_ticket.FilmGo.domain.movie.Movie;
import movie_ticket.FilmGo.domain.movie.enums.MovieStatus;
import movie_ticket.FilmGo.domain.theater.enums.TheaterStatus;
import movie_ticket.FilmGo.domain.thmv.TheaterMovie;
import movie_ticket.FilmGo.file.MovieStore;
import movie_ticket.FilmGo.repository.search.MovieSearch;
import movie_ticket.FilmGo.repository.search.TheaterSearch;
import movie_ticket.FilmGo.service.CommentService;
import movie_ticket.FilmGo.service.MovieService;
import movie_ticket.FilmGo.service.TheaterMovieService;
import movie_ticket.FilmGo.service.TheaterService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final TheaterService theaterService;
    private final MovieStore movieStore;
    private final TheaterMovieService theaterMovieService;
    private final CommentService commentService;

    @GetMapping("/{movieId}")
    public String findMovie(@PathVariable Long movieId, Model model) {
        Movie movie = movieService.findByIdWithViewCount(movieId);
        model.addAttribute("movie", movie);
        model.addAttribute("theaters", theaterService.findAll(new TheaterSearch(null, TheaterStatus.REGISTERED)));
        model.addAttribute("commentList", commentService.findAll(movie));
        return "movies/movieForm";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + movieStore.getFullPath(filename));
    }

    @GetMapping("/list")
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
