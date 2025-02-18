package movie_ticket.FilmGo.controller.login;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.domain.member.Member;
import movie_ticket.FilmGo.service.MemberService;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

import static movie_ticket.FilmGo.controller.login.LoginSession.LOG_ID;
import static movie_ticket.FilmGo.controller.login.LoginSession.USER_ROLE;

@Slf4j
@Controller
@RequestMapping("/auth/kakao")
@RequiredArgsConstructor
public class KaKaoAuthController {

    private final MemberService memberService;
    private final RestTemplate restTemplate;
    private final HttpSession session;

    @GetMapping("/callback")
    public String kakaoCallback(@RequestParam String code, RedirectAttributes redirectAttributes) {
        log.info("🔑 카카오 로그인 인증 코드: {}", code);

        // 1️⃣ 카카오 서버에 Access Token 요청
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "f068e6cff10ce83810e8a1db0880aa26");
        params.add("redirect_uri", "http://localhost:8080/auth/kakao/callback");
        params.add("code", code);

        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);
        Map<String, Object> responseBody = response.getBody();
        String accessToken = (String) responseBody.get("access_token");

        // 2️⃣ 카카오 사용자 정보 요청
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> userInfoRequest = new HttpEntity<>(authHeaders);

        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userInfoRequest, Map.class);
        Map<String, Object> userInfo = userInfoResponse.getBody();

        // 3️⃣ 사용자 정보 추출 (`id`, `nickname`, `profile_image`)
        String kakaoId = userInfo.get("id").toString();
        String nickname = ((Map<String, Object>) userInfo.get("properties")).get("nickname").toString();

        // 4️⃣ DB에서 회원 조회 (없으면 자동 회원가입)
        Member member = memberService.findByKakaoId(kakaoId)
                .orElseGet(() -> {
                    Member newMember = Member.createKakaoMember(kakaoId, nickname);
                    memberService.save(newMember);
                    return newMember;
                });

        // 5️⃣ 세션 저장
        session.setAttribute("KAKAO_ID", member.getKakaoId());
        session.setAttribute("USER_NAME", member.getUsername());
        session.setAttribute(USER_ROLE, member.getRole());
        session.setAttribute("KAKAO_ACCESS_TOKEN", accessToken);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String kakaoLogout() {
        String kakaoId = (String) session.getAttribute("KAKAO_ID");

        if (kakaoId != null) {
            String logoutUrl = "https://kapi.kakao.com/v1/user/logout";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + session.getAttribute("KAKAO_ACCESS_TOKEN"));

            HttpEntity<String> entity = new HttpEntity<>(headers);
            restTemplate.exchange(logoutUrl, HttpMethod.POST, entity, String.class);

            log.info("✅ 카카오 로그아웃 성공!");
        }

        // 세션 초기화 (Spring Security 로그아웃 포함)
        session.invalidate();

        return "redirect:/";
    }
}
