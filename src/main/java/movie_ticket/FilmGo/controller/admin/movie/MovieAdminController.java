package movie_ticket.FilmGo.controller.admin.movie;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.controller.movie.dto.MovieForm;
import movie_ticket.FilmGo.converter.MovieConverter;
import movie_ticket.FilmGo.domain.member.enums.MemberRole;
import movie_ticket.FilmGo.domain.movie.Movie;
import movie_ticket.FilmGo.domain.theater.Theater;
import movie_ticket.FilmGo.domain.theater.enums.TheaterStatus;
import movie_ticket.FilmGo.domain.thmv.TheaterMovie;
import movie_ticket.FilmGo.repository.search.MovieSearch;
import movie_ticket.FilmGo.repository.search.TheaterSearch;
import movie_ticket.FilmGo.service.MovieService;
import movie_ticket.FilmGo.service.TheaterMovieService;
import movie_ticket.FilmGo.service.TheaterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class MovieAdminController {

    private final TheaterService theaterService;
    private final MovieService movieService;
    private final MovieConverter movieConverter;
    private final TheaterMovieService theaterMovieService;

    @GetMapping("")
    public String adminDashboard() {
        return "/admin/dashboard";
    }

    @GetMapping("/movies")
    public String adminMovieForm(Model model, HttpServletRequest request) {
        model.addAttribute("movies", movieService.findAll(new MovieSearch(null, null)));

        HttpSession session = request.getSession(false);
        if (session != null) {
            model.addAttribute("memberRole", (MemberRole) session.getAttribute("userRole"));
        }

        return "admin/movies/movie-manage";
    }

    @GetMapping("/movies/new")
    public String createMovie(@ModelAttribute(name = "form") MovieForm form, Model model) {
        model.addAttribute("theaters", theaterService.findAll(new TheaterSearch(null, TheaterStatus.REGISTERED)));
        return "movies/createForm";
    }

    @PostMapping("/movies/new")
    public String addMovie(@Validated @ModelAttribute(name = "form") MovieForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/admin/movies/new";
        }
        List<Theater> theaters = new ArrayList<>();
        for (Long theaterId : form.getTheaterIds()) {
            theaters.add(theaterService.findById(theaterId));
        }
        Movie movie = movieConverter.toEntity(form);
        movieService.save(movie);

        theaterMovieService.saveMovieAndTheaters(movie, theaters);

        return "redirect:/";
    }

    @GetMapping("/movies/update/{movieId}")
    public String updateMovieForm(@ModelAttribute(name = "form") MovieForm form, @PathVariable Long movieId, Model model) {
        Movie movie = movieService.findById(movieId).get();
        model.addAttribute("movie", movie);
        model.addAttribute("form", movieConverter.toForm(movie));
        model.addAttribute("theaters", theaterService.findAll(new TheaterSearch(null, null)));
        log.info("form ={}", movieConverter.toForm(movie).getTheaterIds());
        return "admin/movies/updateForm";
    }

    @PostMapping("/movies/update/{movieId}")
    public String updateMovie(@ModelAttribute(name = "form") MovieForm form, BindingResult bindingResult,
                              @PathVariable Long movieId) {
        if (bindingResult.hasErrors()) {
            log.info("오류");
            return "admin/movies/update/" + movieId;
        }

        List<Theater> theaters = new ArrayList<>();
        for (Long theaterId : form.getTheaterIds()) {
            theaters.add(theaterService.findById(theaterId));
        }

        Optional<Movie> movie = movieService.findById(movieId);
        Movie entity = movieConverter.toEntity(form);

        Movie updateMovie = movie.get().updateMovie(entity);
        for (TheaterMovie theaterMovie : theaterMovieService.findAllTheaterMovieByMovie(movie.get())) {
            theaterMovieService.deleteTheaterMovie(theaterMovie);
        }

        movieService.save(updateMovie);
        theaterMovieService.saveMovieAndTheaters(movie.get(), theaters);

        return "redirect:/admin/movies";
    }

    @PostMapping("/movies/delete/{movieId}")
    public String deleteMovie(@PathVariable Long movieId) {
        movieService.deleteMovie(movieService.findById(movieId).get());

        return "redirect:/admin/movies";
    }

}
