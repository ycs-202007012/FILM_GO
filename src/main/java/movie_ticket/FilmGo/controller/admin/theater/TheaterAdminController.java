package movie_ticket.FilmGo.controller.admin.theater;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.controller.theater.dto.TheaterForm;
import movie_ticket.FilmGo.converter.TheaterConverter;
import movie_ticket.FilmGo.domain.member.enums.MemberRole;
import movie_ticket.FilmGo.domain.theater.Theater;
import movie_ticket.FilmGo.repository.search.TheaterSearch;
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
@RequestMapping("/admin/theaters")
@RequiredArgsConstructor
public class TheaterAdminController {

    private final TheaterService theaterService;
    private final TheaterConverter theaterConverter;
    private final TheaterMovieService theaterMovieService;

    @GetMapping("/new")
    public String createTheaterForm(@ModelAttribute(name = "form") TheaterForm form) {
        return "admin/theaters/createForm";
    }

    @PostMapping("/new")
    public String addTheater(@ModelAttribute(name = "form") @Validated TheaterForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/theaters/createForm";
        }
        if (theaterService.findByName(form.getName()).isPresent()) {
            bindingResult.rejectValue("name", "same_name", "동일한 이름의 영화관이 존재합니다.");
            return "admin/theaters/createForm";
        }
        Theater entity = theaterConverter.toEntity(form);
        theaterService.save(entity);

        return "redirect:/admin/theaters/theater-manage";
    }

    @GetMapping("")
    public String adminTheaterForm(Model model, HttpServletRequest request) {
        List<Theater> theaters = theaterService.findAll(new TheaterSearch(null, null));
        HttpSession session = request.getSession(false);
        model.addAttribute("theaters", theaters);

        if (session != null) {
            model.addAttribute("memberRole", (MemberRole) session.getAttribute("userRole"));
        }

        return "admin/theaters/theater-manage";
    }

    @GetMapping("/update/{theaterId}")
    public String updateTheaterForm(@ModelAttribute(name = "form") TheaterForm form, @PathVariable Long theaterId, Model model) {
        model.addAttribute("theater", theaterService.findById(theaterId));
        model.addAttribute("form", theaterConverter.toForm(theaterService.findById(theaterId)));
        return "/admin/theaters/updateForm";
    }

    @PostMapping("/update/{theaterId}")
    public String updateTheater(@ModelAttribute(name = "form") TheaterForm form, @PathVariable Long theaterId) {
        theaterService.update(theaterId, form);
        return "redirect:/admin/theaters";
    }

    @PostMapping("/delete/{id}")
    public String deleteTheater(@PathVariable Long id) {
        theaterService.deleteTheater(id);
        return "redirect:/admin/theaters";
    }

    @PostMapping("/hard-delete/{id}")
    public String hardDeleteTheater(@PathVariable Long id) {
        Theater theater = theaterService.findById(id);

        theaterService.hardDeleteTheater(theater);
        /*List<TheaterMovie> theaterMovies = theaterMovieService.findAllByTheater(theater);
        for (TheaterMovie theaterMovie : theaterMovies) {
            theaterMovieService.deleteTheaterMovie(theaterMovie);
        }*/

        return "redirect:/admin/theaters";
    }
}
