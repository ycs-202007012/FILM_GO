package movie_ticket.FilmGo.controller.member.dto;

import lombok.*;
import movie_ticket.FilmGo.domain.member.enums.MemberStatus;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberSearch {

    private String username;
    private MemberStatus status;
}
