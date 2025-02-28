package movie_ticket.FilmGo.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.controller.login.LoginSession;
import movie_ticket.FilmGo.controller.login.dto.KakaoUserInfo;
import movie_ticket.FilmGo.controller.member.dto.MemberForm;
import movie_ticket.FilmGo.controller.member.dto.MemberSearch;
import movie_ticket.FilmGo.domain.member.Member;
import movie_ticket.FilmGo.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long save(Member member) {
        log.info("member 저장:{} <- id", member.getId());
        return memberRepository.save(member);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new RuntimeException("회원 정보가 조회되지 않습니다 ID:" + id));
    }

    public Optional<Member> findByName(String name) {
        return Optional.ofNullable(memberRepository.findByName(name))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 계정입니다."));
    }

    public List<Member> findAll(MemberSearch memberSearch) {
        return memberRepository.findAll(memberSearch.getUsername(), memberSearch.getStatus());
    }

    public Member deleteMember(Long id) {
        return memberRepository.deleteMember(id);
    }

    public Optional<Member> findByKakaoId(String kakaoId) {
        return memberRepository.findByKakaoId(kakaoId);
    }

    public Member findByServletRequest(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Long memberId = (Long) session.getAttribute(LoginSession.LOG_ID);
        if (memberId != null) {
            return memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("일치하는 회원이 없습니다"));
        }
        String kakaoId = (String) session.getAttribute("KAKAO_ID");
        return memberRepository.findByKakaoId(kakaoId).orElseThrow(() -> new RuntimeException("일치하는 회원이 없습니다"));
    }

    public Member findOrCreateKakaoMember(KakaoUserInfo kakaoUserInfo) {
        return memberRepository.findByKakaoId(kakaoUserInfo.getId())
                .orElseGet(() -> {
                    Member newMember = Member.createKakaoMember(
                            kakaoUserInfo.getId(),
                            kakaoUserInfo.getNickname()
                    );
                    Long saveId = memberRepository.save(newMember);
                    return findById(saveId);
                });
    }

    @Transactional
    public void update(Member member, MemberForm form) {
        member.updateMember(member, form);
    }
}
