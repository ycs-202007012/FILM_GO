package movie_ticket.FilmGo.converter;

import lombok.RequiredArgsConstructor;
import movie_ticket.FilmGo.annotation.ExChangeEntity;
import movie_ticket.FilmGo.controller.movie.dto.MovieForm;
import movie_ticket.FilmGo.domain.movie.Movie;
import movie_ticket.FilmGo.domain.movie.enums.MovieStatus;
import movie_ticket.FilmGo.domain.theater.Theater;
import movie_ticket.FilmGo.domain.thmv.TheaterMovie;
import movie_ticket.FilmGo.domain.upload.MovieUploadFile;
import movie_ticket.FilmGo.file.MovieStore;
import movie_ticket.FilmGo.service.MovieService;
import movie_ticket.FilmGo.service.TheaterMovieService;
import movie_ticket.FilmGo.service.TheaterService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExChangeEntity
@RequiredArgsConstructor
public class MovieConverter {

    private final MovieStore movieStore;
    private final MovieService movieService;
    private final TheaterService theaterService;
    private final TheaterMovieService theaterMovieService;

    /**
     * MOVIE IMAGE 파일들 추가 해야됨
     */
    public Movie toEntity(MovieForm form) {

        MovieUploadFile mainImage = movieStore.storeFile(form.getMovieUploadFile());

         Movie movie = Movie.builder()
                .title(form.getTitle())
                .time(form.getTime())
                .director(form.getDirector())
                .price(form.getPrice())
                .status(MovieStatus.ACTIVE)
                .synopsis(form.getSynopsis())
                .movieUploadFile(mainImage)
                .build();


        return movie;
    }

    public MovieForm toForm(Movie movie) {
        List<Long> theaters = new ArrayList<>();
        for (TheaterMovie theaterMovie : movie.getTheaterMovies()) {
            theaters.add(theaterMovie.getTheater().getId());
        }
        return MovieForm.builder()
                .title(movie.getTitle())
                .time(movie.getTime())
                .director(movie.getDirector())
                .price(movie.getPrice())
                .synopsis(movie.getSynopsis())
                .theaterIds(theaters)
                .build();
    }
}
