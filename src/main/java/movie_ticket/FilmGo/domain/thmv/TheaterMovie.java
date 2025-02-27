package movie_ticket.FilmGo.domain.thmv;

import jakarta.persistence.*;
import lombok.*;
import movie_ticket.FilmGo.domain.movie.Movie;
import movie_ticket.FilmGo.domain.theater.Theater;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "theater_movie")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TheaterMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "th_mv_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Setter
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "theater_id")
    private Theater theater;

    @Enumerated(EnumType.STRING)
    private TheaterMovieStatus status;
}
