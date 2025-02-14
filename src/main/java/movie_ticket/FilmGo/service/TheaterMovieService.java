package movie_ticket.FilmGo.service;

import lombok.RequiredArgsConstructor;
import movie_ticket.FilmGo.domain.movie.Movie;
import movie_ticket.FilmGo.domain.theater.Theater;
import movie_ticket.FilmGo.domain.thmv.TheaterMovie;
import movie_ticket.FilmGo.domain.thmv.TheaterMovieStatus;
import movie_ticket.FilmGo.repository.TheaterMovieRepository;
import movie_ticket.FilmGo.repository.search.MovieSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TheaterMovieService {

    private final TheaterMovieRepository theaterMovieRepository;
    private final TheaterService theaterService;

    public List<TheaterMovie> findAllByTheater(Theater theater) {
        return theaterMovieRepository.findAllByTheater(theater);
    }

    @Transactional
    public TheaterMovie save(TheaterMovie theaterMovie) {
        return theaterMovieRepository.save(theaterMovie);
    }

    @Transactional
    public void saveTheaterMovies(Theater theater, List<Movie> movies) {
        // 기존 영화 조회
        Set<Movie> existingMovies = findAllByTheater(theater).stream()
                .map(TheaterMovie::getMovie)
                .collect(Collectors.toSet());

        // 중복되지 않은 영화 필터링
        List<TheaterMovie> theaterMovies = movies.stream()
                .filter(movie -> !existingMovies.contains(movie)) // 중복 검사
                .map(movie -> TheaterMovie.builder()
                        .theater(theater)
                        .movie(movie)
                        .status(TheaterMovieStatus.POSSIBLE)
                        .build())
                .toList();

        // 중복되지 않은 데이터 배치 저장
        if (!theaterMovies.isEmpty()) {
            for (TheaterMovie theaterMovie : theaterMovies) {
                theaterMovieRepository.save(theaterMovie);
            }
        }
    }

    @Transactional
    public void saveMovieAndTheaters(Movie movie, List<Theater> theaters) {
        for (Theater theater : theaters) {
            TheaterMovie theaterMovie = theaterMovieRepository.save(TheaterMovie.builder()
                    .theater(theater)
                    .movie(movie)
                    .status(TheaterMovieStatus.POSSIBLE)
                    .build());
            theater.addTheaterMovie(theaterMovie);
        }
    }

    public List<TheaterMovie> findAllTheaterMovieByMovieSearch(MovieSearch movieSearch) {
        return theaterMovieRepository.findAllTheaterMovieByMovieSearch(movieSearch);
    }
}
