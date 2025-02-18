package movie_ticket.FilmGo.controller.admin.theaterHouse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.controller.theaterHouse.domain.TheaterHouseForm;
import movie_ticket.FilmGo.domain.member.enums.MemberRole;
import movie_ticket.FilmGo.domain.theater.Theater;
import movie_ticket.FilmGo.domain.theater.TheaterHouse;
import movie_ticket.FilmGo.domain.theater.enums.TheaterStatus;
import movie_ticket.FilmGo.repository.search.TheaterSearch;
import movie_ticket.FilmGo.service.SeatService;
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
@RequestMapping("/admin/theater-house")
@RequiredArgsConstructor
public class TheaterHouseAdminController {

    private final TheaterService theaterService;
    private final TheaterHouseService theaterHouseService;
    private final SeatService seatService;

    @GetMapping("/create/{theaterId}") // 상영관 추가 폼
    public String createHouseForm(@ModelAttribute(name = "form") TheaterHouseForm form,
                                  @PathVariable Long theaterId, Model model) {
        model.addAttribute("theaterId", theaterId);
        model.addAttribute("theaters", theaterService.findAll(new TheaterSearch(null, TheaterStatus.REGISTERED)));
        return "admin/theaterHouse/createForm";
    }

    // 상영관 생성 처리
    @PostMapping("/create/{theaterId}")
    public String createHouse(@Validated @ModelAttribute(name = "form") TheaterHouseForm form,
                              BindingResult bindingResult,
                              @PathVariable Long theaterId, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            log.info("오류 발생");
            model.addAttribute("theaters", theaterService.findAll(new TheaterSearch(null, TheaterStatus.REGISTERED)));
            return "admin/theaterHouse/createForm";
        }

        // 중복 상영관 번호 확인
        if (theaterHouseService.findByNumber(form.getHouseName(), theaterId).isPresent()) {
            bindingResult.rejectValue("houseName", "sameNumber", "이미 해당 이름의 상영관이 존재합니다.");
            return "admin/theaterHouse/createForm";
        }

        Theater theater = theaterService.findById(theaterId);

        TheaterHouse theaterHouse = new TheaterHouse(form.getHouseName(), theater, form.getSeat());
        theaterHouseService.save(theaterHouse);

        redirectAttributes.addAttribute("theaterId", theaterId);

        return "redirect:/admin/theater-house/{theaterId}"; // 상영관 목록으로 리다이렉트
    }

    @GetMapping("/update/{theaterHouseId}") // 수정할 상영관의 ID를 기반으로 폼 제공
    public String updateTheaterHouseForm(@PathVariable Long theaterHouseId, Model model) {
        TheaterHouse findHouse = theaterHouseService.findById(theaterHouseId);

        model.addAttribute("theaterHouse", findHouse);
        model.addAttribute("theaterId", findHouse.getTheater().getId()); // 수정할 상영관의 극장 ID 추가
        model.addAttribute("theaters", theaterService.findAll(new TheaterSearch(null, TheaterStatus.REGISTERED)));
        return "admin/theaterHouse/updateForm"; // editForm은 updateForm으로 변경할 수 있음
    }

    // 상영관 수정 처리
    @PostMapping("/update/{theaterHouseId}")
    public String updateTheaterHouse(@Validated @ModelAttribute(name = "theaterHouse") TheaterHouseForm form,
                                     BindingResult bindingResult,
                                     @PathVariable Long theaterHouseId,
                                     RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            TheaterHouse findHouse = theaterHouseService.findById(theaterHouseId);
            redirectAttributes.addAttribute("theaterId", findHouse.getTheater().getId());
            return "admin/theaterHouse/updateForm";
        }

        Optional<TheaterHouse> existingHouse = theaterHouseService.findByNumber(form.getHouseName(), form.getTheaterId());
        if (existingHouse.isPresent() && !existingHouse.get().getId().equals(theaterHouseId)) {
            bindingResult.rejectValue("houseNumber", "sameNumber", "이미 해당 이름의 상영관이 존재합니다.");
            return "admin/theaterHouse/updateForm";
        }

        TheaterHouse theaterHouse = theaterHouseService.findById(theaterHouseId);
        theaterHouseService.update(form, theaterHouse);

        redirectAttributes.addAttribute("theaterId", theaterHouse.getTheater().getId());

        return "redirect:/admin/theater-house/{theaterId}"; // 상영관 목록으로 리다이렉트
    }

    // 상영관 목록 보기
    @GetMapping("/{theaterId}") // 해당 theaterId의 상영관 목록을 조회
    public String getTheaterHouses(@PathVariable Long theaterId, Model model, HttpServletRequest request) {
        List<TheaterHouse> theaterHouses = theaterHouseService.findAll(theaterService.findById(theaterId));
        HttpSession session = request.getSession(false);

        model.addAttribute("theaterHouses", theaterHouses);
        if (session != null) {
            model.addAttribute("memberRole", (MemberRole) session.getAttribute("userRole"));
        }
        return "admin/theaterHouse/theaterHouse-manage";
    }

    @PostMapping("/delete/{theaterHouseId}")
    public String deleteTheaterHouse(@PathVariable Long theaterHouseId, RedirectAttributes redirectAttributes) {
        TheaterHouse theaterHouse = theaterHouseService.findById(theaterHouseId);
        theaterHouseService.delete(theaterHouse);

        redirectAttributes.addAttribute("theaterId", theaterHouse.getTheater().getId()); // 삭제 후 극장 ID 추가

        return "redirect:/admin/theater-house/{theaterId}"; // 상영관 목록으로 리다이렉트
    }
}
