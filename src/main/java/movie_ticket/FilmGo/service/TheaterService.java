package movie_ticket.FilmGo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.controller.theater.dto.TheaterForm;
import movie_ticket.FilmGo.converter.TheaterConverter;
import movie_ticket.FilmGo.domain.theater.Theater;
import movie_ticket.FilmGo.domain.thmv.TheaterMovie;
import movie_ticket.FilmGo.repository.MovieRepository;
import movie_ticket.FilmGo.repository.TheaterRepository;
import movie_ticket.FilmGo.repository.search.TheaterSearch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TheaterService {

    private final TheaterRepository theaterRepository;
    private final TheaterMovieService theaterMovieService;
    private final MovieRepository movieRepository;

    @Transactional
    public Theater save(Theater theater) {
        log.info("[{}] <- THEATER 객체 저장!", theater);
        return theaterRepository.save(theater);
    }

    public Theater findById(Long id) {
        Theater theater = theaterRepository.findById(id);
        log.info("[{}] <- THEATER 객체 ID로 조회", theater);
        return theater;
    }

    public Optional<Theater> findByName(String name) {
        Optional<Theater> theater = theaterRepository.findByName(name);
        log.info("[{}] <- THEATER 객체 이름으로 조회", theater);
        return theater;
    }

    @Transactional
    public void update(Long theaterId, TheaterForm form) {
        Theater theater = findById(theaterId);
        theater.updateTheater(form);
    }

    public List<Theater> findAllByName(TheaterSearch theaterSearch) {
        List<Theater> theaterList = theaterRepository.findAll(theaterSearch);
        log.info("[{}] <- THEATER 객체 LIST {}  <- 해당 검색어로 조회", theaterList, theaterSearch.getName());
        return theaterList;
    }

    public List<Theater> findAll(TheaterSearch theaterSearch) {
        List<Theater> theaterList = theaterRepository.findAll(theaterSearch);
        log.info("[{}] <- THEATER 객체 LIST {}  <- 해당 검색어로 조회", theaterList, theaterSearch);
        return theaterList;
    }

    @Transactional
    public void deleteTheater(Long id) {
        Theater theater = findById(id);
        theater.deleteTheater();
    }

    @Transactional
    public void hardDeleteTheater(Theater theater) {
        theaterRepository.deleteTheater(theater);
    }

    public Long getDefaultTheaterId(Long movieId, Long theaterId) {
        List<TheaterMovie> theaterMovieList = theaterMovieService.findAllTheaterMovieByMovie(
                movieRepository.findById(movieId).get()
        );

        /*if (theaterId == null && !theaterMovieList.isEmpty()) {
            return theaterMovieList.get(0).getTheater().getId();
        }*/
        return theaterId;
    }

}
