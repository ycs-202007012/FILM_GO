package movie_ticket.FilmGo.controller.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.controller.login.dto.LoginForm;
import movie_ticket.FilmGo.domain.member.Member;
import movie_ticket.FilmGo.service.LoginService;
import movie_ticket.FilmGo.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

import static movie_ticket.FilmGo.controller.login.LoginSession.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final MemberService memberService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute(name = "form") LoginForm form) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute(name = "form") LoginForm form, BindingResult bindingResult,
                        HttpServletRequest request, @RequestParam(defaultValue = "/") String redirectURL,RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors() || loginService.loginCheck(form)) {
            log.info("LOGIN CHECK 오류");
            return "login/loginForm";
        }

        Optional<Member> member = memberService.findByName(form.getName());

        HttpSession session = request.getSession();

        session.setAttribute(LOG_ID, member.get().getId());
        session.setAttribute(USER_ROLE, member.get().getRole());
        session.setAttribute("USER_NAME",member.get().getUsername());

        redirectAttributes.addFlashAttribute("message", "로그인 성공!");

        return "redirect:" + redirectURL;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
