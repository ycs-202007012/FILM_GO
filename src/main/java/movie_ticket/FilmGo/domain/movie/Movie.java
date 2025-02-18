package movie_ticket.FilmGo.domain.movie;

import jakarta.persistence.*;
import lombok.*;
import movie_ticket.FilmGo.domain.movie.enums.MovieStatus;
import movie_ticket.FilmGo.domain.theater.Theater;
import movie_ticket.FilmGo.domain.thmv.TheaterMovie;
import movie_ticket.FilmGo.domain.upload.MovieUploadFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private Long id;
    private String title;
    private Integer time;
    private LocalDate releaseDate;
    private String director;
    private Integer price;
    private int viewCount = 0;

    @Enumerated(EnumType.STRING)
    private MovieStatus status;

    @Column(columnDefinition = "TEXT")
    private String synopsis;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movie")
    private List<TheaterMovie> theaterMovies = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "movie_upload_file_id")
    private MovieUploadFile movieUploadFile;

    public Movie updateMovie(Movie movie) {
        this.title = movie.getTitle();
        this.time = movie.getTime();
        this.director = movie.getDirector();
        this.price = movie.getPrice();
        this.movieUploadFile = movie.getMovieUploadFile();
        this.movieUploadFile.setMovie(this);
        return this;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

}
