package movie_ticket.FilmGo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.controller.member.dto.MemberSearch;
import movie_ticket.FilmGo.domain.member.Member;
import movie_ticket.FilmGo.repository.MemberRepository;
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

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(memberRepository.findById(id));
    }

    public Optional<Member> findByName(String name){
        return Optional.ofNullable(memberRepository.findByName(name))
                .orElseThrow(() -> new RuntimeException("존재하지 않는 계정입니다."));
    }
    public List<Member> findAll(MemberSearch memberSearch){
        return memberRepository.findAll(memberSearch.getUsername(),memberSearch.getStatus());
    }

    public Member deleteMember(Long id){
        return memberRepository.deleteMember(id);
    }
}
