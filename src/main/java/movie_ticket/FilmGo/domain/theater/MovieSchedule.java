package movie_ticket.FilmGo.domain.theater;

import jakarta.persistence.*;
import lombok.*;
import movie_ticket.FilmGo.domain.movie.Movie;
import movie_ticket.FilmGo.domain.theater.enums.MovieScheduleStatus;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MovieSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_schedule_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "theater_house_id")
    private TheaterHouse theaterHouse;

    @Embedded
    private StartEndTime time;

    @Enumerated(EnumType.STRING)
    private MovieScheduleStatus status;

    public MovieSchedule(Movie movie, TheaterHouse theaterHouse, StartEndTime time) {
        this.movie = movie;
        this.theaterHouse = theaterHouse;
        this.time = time;
        theaterHouse.addMovieSchedule(this);
    }

}
