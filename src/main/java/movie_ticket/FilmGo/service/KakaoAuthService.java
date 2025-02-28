package movie_ticket.FilmGo.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import movie_ticket.FilmGo.controller.login.dto.KakaoUserInfo;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final RestTemplate restTemplate;
    private final String CLIENT_ID = "f068e6cff10ce83810e8a1db0880aa26";
    private final String REDIRECT_URI = "http://localhost:8080/auth/kakao/callback";

    private static final String KAKAO_LOGOUT_URL = "https://kapi.kakao.com/v1/user/logout";

    public boolean logoutFromKakao(String accessToken) {
        if (accessToken == null) {
            return false;
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);

            restTemplate.exchange(KAKAO_LOGOUT_URL, HttpMethod.POST, new HttpEntity<>(headers), String.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getAccessToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", CLIENT_ID);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);
        return (String) response.getBody().get("access_token");
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, Map.class);
        Map<String, Object> userInfo = response.getBody();

        String kakaoId = userInfo.get("id").toString();
        String nickname = ((Map<String, Object>) userInfo.get("properties")).get("nickname").toString();

        return new KakaoUserInfo(kakaoId, nickname, accessToken);
    }

    public void logout(HttpSession session) {
        if (session == null) return;

        String accessToken = (String) session.getAttribute("KAKAO_ACCESS_TOKEN");

        logoutFromKakao(accessToken);

        session.invalidate();
    }
}

