package movie_ticket.FilmGo.controller.movieSchedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.controller.movieSchedule.domain.ScheduleForm;
import movie_ticket.FilmGo.domain.movie.Movie;
import movie_ticket.FilmGo.domain.theater.MovieSchedule;
import movie_ticket.FilmGo.domain.theater.StartEndTime;
import movie_ticket.FilmGo.domain.theater.Theater;
import movie_ticket.FilmGo.domain.theater.TheaterHouse;
import movie_ticket.FilmGo.domain.thmv.TheaterMovie;
import movie_ticket.FilmGo.service.*;
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
@RequestMapping("/movieSchedule")
@RequiredArgsConstructor
public class MovieScheduleController {

    private final MovieScheduleService movieScheduleService;
    private final TheaterHouseService theaterHouseService;
    private final TheaterMovieService theaterMovieService;
    private final TheaterService theaterService;
    private final MovieService movieService;

    @GetMapping("/{theaterId}/new")
    public String addMovieScheduleForm(@ModelAttribute("form") ScheduleForm form, Model model, @PathVariable Long theaterId) {
        Theater theater = theaterService.findById(theaterId);
        List<Movie> movies = new ArrayList<>();
        for (TheaterMovie theaterMovie : theaterMovieService.findAllByTheater(theater)) {
            movies.add(theaterMovie.getMovie());
        }
        model.addAttribute("movies", movies);
        model.addAttribute("theaterHouses", theaterHouseService.findAll(theater));
        return "schedules/createForm";
    }

    @PostMapping("/{theaterId}/new")
    public String addMovieSchedule(@ModelAttribute("form") @Validated ScheduleForm form, BindingResult bindingResult, Model model, @PathVariable Long theaterId) {

        if (bindingResult.hasErrors()) {
            return "schedules/createForm";
        }

        TheaterHouse theaterHouse = theaterHouseService.findById(form.getTheaterHouseId());
        Theater theater = theaterService.findById(theaterId);
        List<TheaterMovie> theaterMovies = theaterMovieService.findAllByTheater(theater);

        List<Movie> movies = movieService.findMoviesByTheaterMovieList(theaterMovies);
        model.addAttribute("movies", movies);

        Optional<Movie> movie1 = movieService.findById(form.getMovieId());
        if (movieScheduleService.checkScheduleTime(theaterHouse, new StartEndTime(form.getStartTime(), form.getStartTime().plusMinutes(movie1.get().getTime())))) {
            bindingResult.reject("dayNotMatch", "이미 존재하는 시간대 입니다");

            extracted(model, form, movies, theater);
            return "schedules/createForm";
        }

        Optional<Movie> movie = movieService.findById(form.getMovieId());

        MovieSchedule entity = movieScheduleService.createMovieSchedule(movie.get(), theaterHouse,
                new StartEndTime(form.getStartTime(), form.getStartTime().plusMinutes(movie1.get().getTime())));
        movieScheduleService.save(entity);

        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String findScheduleForm(@PathVariable Long id, Model model) {
        MovieSchedule findSchedule = movieScheduleService.findById(id);
        model.addAttribute("movieSchedule", findSchedule);
        return "schedules/scheduleForm";
    }

    @PostMapping("/delete/{id}")
    public String removeSchedule(@PathVariable Long id) {
        MovieSchedule schedule = movieScheduleService.findById(id);
        movieScheduleService.removeSchedule(schedule);

        return "redirect:/";
    }

    private void extracted(Model model, ScheduleForm form, List<Movie> movies, Theater theater) {
        model.addAttribute("form", form);
        model.addAttribute("movies", movies);
        model.addAttribute("theaterHouses", theaterHouseService.findAll(theater));
    }
}
