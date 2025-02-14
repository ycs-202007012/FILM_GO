package movie_ticket.FilmGo.controller.movieSchedule.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import movie_ticket.FilmGo.domain.movie.Movie;
import movie_ticket.FilmGo.domain.theater.TheaterHouse;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ScheduleForm {

    @NotNull
    private Long movieId;

    @NotNull
    private Long theaterHouseId;

    @NotNull
    private LocalDateTime startTime;
    @NotNull
    private LocalDateTime endTime;
}
