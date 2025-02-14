package movie_ticket.FilmGo.controller.movieSchedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.controller.movieSchedule.domain.ScheduleForm;
import movie_ticket.FilmGo.converter.TheaterScheduleConverter;
import movie_ticket.FilmGo.domain.theater.MovieSchedule;
import movie_ticket.FilmGo.domain.theater.StartEndTime;
import movie_ticket.FilmGo.domain.theater.TheaterHouse;
import movie_ticket.FilmGo.service.MovieScheduleService;
import movie_ticket.FilmGo.service.TheaterHouseService;
import movie_ticket.FilmGo.service.TheaterMovieService;
import movie_ticket.FilmGo.service.TheaterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/movieSchedule")
@RequiredArgsConstructor
public class MovieScheduleController {

    private final MovieScheduleService movieScheduleService;
    private final TheaterScheduleConverter movieScheduleConverter;
    private final TheaterHouseService theaterHouseService;
    private final TheaterService theaterService;
    private final TheaterMovieService theaterMovieService;

    @GetMapping("/{theaterId}/new")
    public String addMovieScheduleForm(@ModelAttribute("form") ScheduleForm form, Model model, @PathVariable Long theaterId) {
        return "schedules/createForm";
    }

    @PostMapping("/{theaterId}/new")
    public String addMovieSchedule(@Validated ScheduleForm form, BindingResult bindingResult, Model model, @PathVariable Long theaterId) {
        if (bindingResult.hasErrors()) {
            return "schedules/createForm";
        }
        TheaterHouse theaterHouse = theaterHouseService.findById(form.getTheaterHouseId());
        if (movieScheduleService.checkScheduleTime(theaterHouse, new StartEndTime(form.getStartTime(), form.getEndTime()))) {
            bindingResult.rejectValue("startTime", "timeError", "이미 존재하는 시간대 입니다");
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
}
