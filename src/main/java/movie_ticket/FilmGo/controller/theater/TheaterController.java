package movie_ticket.FilmGo.controller.theater;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.controller.theater.dto.TheaterForm;
import movie_ticket.FilmGo.controller.theaterHouse.domain.TheaterHouseForm;
import movie_ticket.FilmGo.converter.TheaterHouseConverter;
import movie_ticket.FilmGo.domain.member.enums.MemberRole;
import movie_ticket.FilmGo.domain.theater.Theater;
import movie_ticket.FilmGo.domain.theater.TheaterHouse;
import movie_ticket.FilmGo.domain.theater.enums.TheaterStatus;
import movie_ticket.FilmGo.repository.search.TheaterSearch;
import movie_ticket.FilmGo.service.MovieScheduleService;
import movie_ticket.FilmGo.service.TheaterHouseService;
import movie_ticket.FilmGo.service.TheaterMovieService;
import movie_ticket.FilmGo.service.TheaterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(path = {"/theaters", "/theater"})
@RequiredArgsConstructor
public class TheaterController {
    private final TheaterHouseConverter theaterHouseConverter;
    private final TheaterHouseService theaterHouseService;
    private final TheaterService theaterService;
    private final TheaterMovieService theaterMovieService;
    private final MovieScheduleService movieScheduleService;

    @GetMapping("/list")
    public String AllTheater(TheaterSearch theaterSearch, Model model, HttpServletRequest request) {
        List<Theater> theaters = theaterService.findAll(new TheaterSearch(null, TheaterStatus.REGISTERED));
        HttpSession session = request.getSession(false);
        model.addAttribute("theaters", theaters);

        // 선택된 극장 정보 전달 (초기에는 null)
        if (theaterSearch.getName() != null) {
            Theater selectedTheater = theaterService.findByName(theaterSearch.getName()).get();

            model.addAttribute("selectedTheater", selectedTheater);
            log.info("✅ 극장 정보: {}", selectedTheater);
            log.info("✅ 극장에서 상영 중인 영화 개수: {}", selectedTheater.getTheaterMovies().size());
        }
        if (session != null) {
            model.addAttribute("memberRole", (MemberRole) session.getAttribute("userRole"));
        }
        return "theaters/listForm";
    }

    @GetMapping("/{id}")
    public String findTheater(@PathVariable Long id, Model model) {
        Theater theater = theaterService.findById(id);
        model.addAttribute("theater", theater);
        return "theaters/theaterForm";
    }
}
