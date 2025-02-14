package movie_ticket.FilmGo.controller.theaterMovie;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.domain.movie.Movie;
import movie_ticket.FilmGo.domain.movie.enums.MovieStatus;
import movie_ticket.FilmGo.domain.theater.Theater;
import movie_ticket.FilmGo.domain.thmv.TheaterMovie;
import movie_ticket.FilmGo.repository.search.MovieSearch;
import movie_ticket.FilmGo.service.MovieService;
import movie_ticket.FilmGo.service.TheaterMovieService;
import movie_ticket.FilmGo.service.TheaterService;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("theater-movies")
@RequiredArgsConstructor
public class TheaterMovieController {

    private final TheaterMovieService theaterMovieService;
    private final TheaterService theaterService;
    private final MovieService movieService;

    @GetMapping("/{theaterId}/add")
    public String addMovieForm(@PathVariable Long theaterId, Model model) {
        Theater theater = theaterService.findById(theaterId);
        model.addAttribute("theater", theater);
        model.addAttribute("movies", movieService.findAll(new MovieSearch(null, MovieStatus.ACTIVE)));
        return "theaterMovie/addForm";
    }

    @PostMapping("/add")
    public String addTheaterMovie(@RequestParam Long theaterId, @RequestParam List<Long> movieIds, Model model) {
        if (theaterId == null || movieIds == null || movieIds.isEmpty()) {
            log.info("값이 비어있음");
            model.addAttribute("theater", theaterService.findById(theaterId));
            model.addAttribute("movies", movieService.findAll(new MovieSearch(null, MovieStatus.ACTIVE)));
            return "theaterMovie/addForm";
        }
        Theater theater = theaterService.findById(theaterId);

        List<Movie> movies = movieService.findByIds(movieIds);

        theaterMovieService.saveTheaterMovies(theater, movies);
        return "redirect:/theater-movies/list";
    }
}
