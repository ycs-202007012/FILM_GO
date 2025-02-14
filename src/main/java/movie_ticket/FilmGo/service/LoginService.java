package movie_ticket.FilmGo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.controller.login.dto.LoginForm;
import movie_ticket.FilmGo.domain.member.Member;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    public boolean loginCheck(LoginForm form) {
        Optional<Member> findMember = memberService.findByName(form.getName());
        if (findMember.isEmpty()) {
            log.info("[{}] <- 존재하지 않는 계정입니다", form.getName());
            return true;
        }
        if (!passwordEncoder.matches(form.getPassword(), findMember.get().getPassword())) {
            log.info("비밀번호가 일치하지 않습니다.");
            return true;
        }
        return false;
    }

}
