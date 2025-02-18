package movie_ticket.FilmGo.converter;

import lombok.RequiredArgsConstructor;
import movie_ticket.FilmGo.annotation.ExChangeEntity;
import movie_ticket.FilmGo.controller.movieSchedule.domain.ScheduleForm;
import movie_ticket.FilmGo.domain.theater.MovieSchedule;
import movie_ticket.FilmGo.domain.theater.StartEndTime;
import movie_ticket.FilmGo.domain.theater.TheaterHouse;
import movie_ticket.FilmGo.domain.theater.enums.MovieScheduleStatus;
import movie_ticket.FilmGo.service.MovieService;

@ExChangeEntity
@RequiredArgsConstructor
public class TheaterScheduleConverter {

    private final MovieService movieService;

    public MovieSchedule toEntity(ScheduleForm form, TheaterHouse theaterHouse) {
        return MovieSchedule.builder()
                .movie(movieService.findById(form.getMovieId()).get())
                .theaterHouse(theaterHouse)
                .time(new StartEndTime(form.getStartTime(), form.getEndTime()))
                .status(MovieScheduleStatus.REGISTERED)
                .build();
    }
}
