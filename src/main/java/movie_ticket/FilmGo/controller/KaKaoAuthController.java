package movie_ticket.FilmGo.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.repository.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/auth/kakao")
@RequiredArgsConstructor
public class KaKaoAuthController {

    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;
    private HttpSession session;

    @GetMapping("/callback")
    public String kakaoCallback(@RequestParam String code, RedirectAttributes redirectAttributes)
    log.info("카카")
}
