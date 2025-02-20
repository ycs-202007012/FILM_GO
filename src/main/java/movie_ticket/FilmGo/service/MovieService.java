package movie_ticket.FilmGo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.controller.movie.dto.MovieForm;
import movie_ticket.FilmGo.converter.MovieConverter;
import movie_ticket.FilmGo.domain.movie.Movie;
import movie_ticket.FilmGo.domain.movie.enums.MovieStatus;
import movie_ticket.FilmGo.domain.upload.MovieUploadFile;
import movie_ticket.FilmGo.file.MovieStore;
import movie_ticket.FilmGo.repository.MovieRepository;
import movie_ticket.FilmGo.repository.search.MovieSearch;
import movie_ticket.FilmGo.repository.search.TheaterSearch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    @Transactional
    public Movie save(Movie movie) {
        log.info("[{}] <- 영화 저장 성공", movie.getTitle());
        movie.getMovieUploadFile().setMovie(movie);

        return movieRepository.save(movie);
    }

    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }

    @Transactional
    public Movie findByIdWithViewCount(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("해당 영화가 존재하지 않습니다: ID " + movieId));

        movie.increaseViewCount(); // ✅ 조회수 증가
        return movie;
    }

    public List<Movie> findByIds(List<Long> ids){
        return movieRepository.findByIds(ids);
    }

    public List<Movie> findAllByTitle(String title) {
        return movieRepository.findAllByTitle(title);
    }

    public List<Movie> findAll(MovieSearch movieSearch){
        return movieRepository.findAll(movieSearch)
                .stream()
                .sorted(Comparator.comparing(Movie::getViewCount).reversed())
                .toList();
    }

    public List<Movie> findByMovieStatusByActive() {
        return movieRepository.findAll(new MovieSearch(null, MovieStatus.ACTIVE));
    }

    public List<Movie> getActiveMovies() {
        return movieRepository.findAll(new MovieSearch(null, MovieStatus.ACTIVE));
    }

    // 기본 선택된 영화 ID 가져오기
    public Long getDefaultMovieId(Long movieId, List<Movie> movies) {
        if (movieId == null && !movies.isEmpty()) {
            return movies.get(0).getId();
        }
        return movieId;
    }

}
