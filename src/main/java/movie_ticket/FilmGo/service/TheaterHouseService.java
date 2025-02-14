package movie_ticket.FilmGo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.controller.theaterHouse.domain.TheaterHouseForm;
import movie_ticket.FilmGo.domain.theater.Theater;
import movie_ticket.FilmGo.domain.theater.TheaterHouse;
import movie_ticket.FilmGo.repository.TheaterHouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
// sse시스템 만들기
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TheaterHouseService {

    private final TheaterHouseRepository theaterHouseRepository;
    private final TheaterService theaterService;

    @Transactional(readOnly = false)
    public TheaterHouse save(TheaterHouse theaterHouse) {
        log.info("[{}]영화관에 [{}]상영관 등록", theaterHouse.getTheater(), theaterHouse.getHouseNumber());
        return theaterHouseRepository.save(theaterHouse);
    }

    public TheaterHouse findById(Long id) {
        return theaterHouseRepository.findById(id);
    }

    public Optional<TheaterHouse> findByNumber(String number, Long theaterId) {
        return theaterHouseRepository.findByNumber(number, theaterId);
    }

    public List<TheaterHouse> findAll(Theater theater) {
        return theaterHouseRepository.findAll(theater);
    }

    @Transactional(readOnly = false)
    public void update(TheaterHouseForm form, TheaterHouse theaterHouse) {
        theaterHouse.update(form);
    }

    @Transactional(readOnly = false)
    public void delete(TheaterHouse theaterHouse) {
        theaterHouseRepository.delete(theaterHouse);
    }
}
