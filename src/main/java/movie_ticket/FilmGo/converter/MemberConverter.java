package movie_ticket.FilmGo.converter;

import lombok.RequiredArgsConstructor;
import movie_ticket.FilmGo.annotation.ExChangeEntity;
import movie_ticket.FilmGo.controller.member.dto.MemberForm;
import movie_ticket.FilmGo.domain.member.Member;
import movie_ticket.FilmGo.domain.member.enums.MemberRole;
import movie_ticket.FilmGo.domain.member.enums.MemberStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExChangeEntity
@RequiredArgsConstructor
public class MemberConverter {

    private final PasswordEncoder encode;
    public Member toEntity(MemberForm form) {
        return Member.builder()
                .name(form.getName())
                .password(encode.encode(form.getPassword()))
                .username(form.getUsername())
                .age(form.getAge())
                .status(MemberStatus.REGISTERED)
                .role(MemberRole.USER)
                .phoneNumber(form.getPhoneNumber())
                .build();
    }
}
