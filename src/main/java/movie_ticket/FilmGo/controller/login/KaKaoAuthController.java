package movie_ticket.FilmGo.controller.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.controller.login.dto.KakaoUserInfo;
import movie_ticket.FilmGo.domain.member.Member;
import movie_ticket.FilmGo.service.KakaoAuthService;
import movie_ticket.FilmGo.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static movie_ticket.FilmGo.controller.login.LoginSession.USER_ROLE;

@Slf4j
@Controller
@RequestMapping("/auth/kakao")
@RequiredArgsConstructor
public class KaKaoAuthController {

    private final MemberService memberService;
    private final KakaoAuthService kakaoAuthService;

    @GetMapping("/callback")
    public String kakaoCallback(@RequestParam String code, HttpServletRequest request) {

        String accessToken = kakaoAuthService.getAccessToken(code);

        KakaoUserInfo kakaoUserInfo = kakaoAuthService.getUserInfo(accessToken);

        Member member = memberService.findOrCreateKakaoMember(kakaoUserInfo);

        HttpSession session = request.getSession();
        session.setAttribute(LoginSession.LOG_ID, member.getId());
        session.setAttribute("KAKAO_ID", member.getKakaoId());
        session.setAttribute("USER_NAME", member.getUsername());
        session.setAttribute(USER_ROLE, member.getRole());

        return "redirect:/";
    }


    @GetMapping("/logout")
    public String kakaoLogout(HttpServletRequest request) {
        kakaoAuthService.logout(request.getSession(false));
        return "redirect:/";
    }

}
