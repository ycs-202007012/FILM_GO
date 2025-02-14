package movie_ticket.FilmGo.controller.theaterHouse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.controller.theaterHouse.domain.TheaterHouseForm;
import movie_ticket.FilmGo.controller.theaterHouse.domain.TheaterHouseResponse;
import movie_ticket.FilmGo.converter.TheaterHouseConverter;
import movie_ticket.FilmGo.domain.theater.TheaterHouse;
import movie_ticket.FilmGo.domain.theater.enums.TheaterStatus;
import movie_ticket.FilmGo.repository.search.TheaterSearch;
import movie_ticket.FilmGo.service.TheaterHouseService;
import movie_ticket.FilmGo.service.TheaterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/theater-house") // 상영관 관련 모든 작업을 theater-house로 묶음
@RequiredArgsConstructor
public class TheaterHouseController {

    private final TheaterHouseService theaterHouseService;
    private final TheaterHouseConverter theaterHouseConverter;
    private final TheaterService theaterService;

    // 상영관 생성

    // 상영관 수정 폼


    // 상영관 삭제

}
