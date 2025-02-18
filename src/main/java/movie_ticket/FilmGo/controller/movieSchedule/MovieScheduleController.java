package movie_ticket.FilmGo.controller.movieSchedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.controller.movieSchedule.domain.ScheduleForm;
import movie_ticket.FilmGo.converter.TheaterScheduleConverter;
import movie_ticket.FilmGo.domain.movie.Movie;
import movie_ticket.FilmGo.domain.theater.MovieSchedule;
import movie_ticket.FilmGo.domain.theater.StartEndTime;
import movie_ticket.FilmGo.domain.theater.Theater;
import movie_ticket.FilmGo.domain.theater.TheaterHouse;
import movie_ticket.FilmGo.domain.thmv.TheaterMovie;
import movie_ticket.FilmGo.service.MovieScheduleService;
import movie_ticket.FilmGo.service.TheaterHouseService;
import movie_ticket.FilmGo.service.TheaterMovieService;
import movie_ticket.FilmGo.service.TheaterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/movieSchedule")
@RequiredArgsConstructor
public class MovieScheduleController {

    private final MovieScheduleService movieScheduleService;
    private final TheaterScheduleConverter movieScheduleConverter;
    private final TheaterHouseService theaterHouseService;
    private final TheaterMovieService theaterMovieService;
    private final TheaterService theaterService;

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
    public String addMovieSchedule(@Validated ScheduleForm form, BindingResult bindingResult, Model model, @PathVariable Long theaterId) {
        if (bindingResult.hasErrors()) {
            return "schedules/createForm";
        }
        TheaterHouse theaterHouse = theaterHouseService.findById(form.getTheaterHouseId());
        Theater theater = theaterService.findById(theaterId);

        List<Movie> movies = new ArrayList<>();
        for (TheaterMovie theaterMovie : theaterMovieService.findAllByTheater(theater)) {
            movies.add(theaterMovie.getMovie());
        }
        model.addAttribute("movies", movies);

        if (form.getEndTime().isBefore(form.getStartTime()) || form.getEndTime().equals(form.getStartTime())) {
            bindingResult.reject("dayNotMatch", "날짜와 시간을 다시 등록해주세요");

            extracted(model, movies, theater);
            return "schedules/createForm";
        }

        if (movieScheduleService.checkScheduleTime(theaterHouse, new StartEndTime(form.getStartTime(), form.getEndTime()))) {
            bindingResult.reject("dayNotMatch", "이미 존재하는 시간대 입니다");

            extracted(model, movies, theater);
            return "schedules/createForm";
        }

        MovieSchedule entity = movieScheduleConverter.toEntity(form, theaterHouse);
        movieScheduleService.save(entity);

        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String findScheduleForm(@PathVariable Long id, Model model) {
        MovieSchedule findSchedule = movieScheduleService.findById(id);
        model.addAttribute("movieSchedule", findSchedule);
        return "schedules/scheduleForm";
    }

    private void extracted(Model model, List<Movie> movies, Theater theater) {
        model.addAttribute("form", new ScheduleForm(null, null, null, null));
        model.addAttribute("movies", movies);
        model.addAttribute("theaterHouses", theaterHouseService.findAll(theater));
    }
}
