package movie_ticket.FilmGo.controller;

import lombok.RequiredArgsConstructor;
import movie_ticket.FilmGo.controller.login.LoginSession;
import movie_ticket.FilmGo.domain.member.Member;
import movie_ticket.FilmGo.domain.member.enums.MemberRole;
import movie_ticket.FilmGo.domain.movie.enums.MovieStatus;
import movie_ticket.FilmGo.repository.search.MovieSearch;
import movie_ticket.FilmGo.service.MemberService;
import movie_ticket.FilmGo.service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;
    private final MovieService movieService;

    @GetMapping("/")
    public String home(@SessionAttribute(name = LoginSession.USER_ROLE, required = false) MemberRole role,
                       @SessionAttribute(name = LoginSession.LOG_ID, required = false) Long id, Model model) {

        model.addAttribute("movies", movieService.findAll(new MovieSearch(null, MovieStatus.ACTIVE)));
        if (role == null) {
            return "home";
        }
        if (role.equals(MemberRole.MASTER)) {
            Optional<Member> member = memberService.findById(id);
            model.addAttribute("member", member.get());
            return "home";
        }


        return "home";
    }
}
