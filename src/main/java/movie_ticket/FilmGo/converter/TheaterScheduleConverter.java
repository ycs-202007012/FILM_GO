package movie_ticket.FilmGo.converter;

import lombok.RequiredArgsConstructor;
import movie_ticket.FilmGo.annotation.ExChangeEntity;
import movie_ticket.FilmGo.controller.movieSchedule.domain.ScheduleForm;
import movie_ticket.FilmGo.domain.movie.Movie;
import movie_ticket.FilmGo.domain.theater.MovieSchedule;
import movie_ticket.FilmGo.domain.theater.StartEndTime;
import movie_ticket.FilmGo.domain.theater.TheaterHouse;
import movie_ticket.FilmGo.domain.theater.enums.MovieScheduleStatus;
import movie_ticket.FilmGo.service.MovieScheduleService;
import movie_ticket.FilmGo.service.MovieService;

import java.util.Optional;

@ExChangeEntity
@RequiredArgsConstructor
public class TheaterScheduleConverter {

    private final MovieService movieService;
    private final MovieScheduleService movieScheduleService;

    public MovieSchedule toEntity(ScheduleForm form, TheaterHouse theaterHouse) {
        Optional<Movie> movie = movieService.findById(form.getMovieId());
        return MovieSchedule.builder()
                .movie(movie.get())
                .theaterHouse(theaterHouse)
                .time(new StartEndTime(form.getStartTime(), form.getStartTime().plusMinutes(movie.get().getTime())))
                .status(MovieScheduleStatus.REGISTERED)
                .build();
    }
}
