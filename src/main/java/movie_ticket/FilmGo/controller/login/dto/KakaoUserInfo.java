package movie_ticket.FilmGo.controller.login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoUserInfo {

    private String id;
    private String nickname;
    private String accessToken;
}
