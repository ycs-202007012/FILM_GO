package movie_ticket.FilmGo.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import movie_ticket.FilmGo.domain.member.Member;
import movie_ticket.FilmGo.domain.member.enums.MemberStatus;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static movie_ticket.FilmGo.domain.member.QMember.member;


@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    public Optional<Member> findByName(String name) {
        try {
            Member member = em.createQuery("select m from Member m where m.name = :name", Member.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.ofNullable(member);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Member> findAll(String username, MemberStatus status) {
        return query.select(member)
                .from(member)
                .where(likeName(username), member.status.eq(status))
                .fetch();
    }

    public Member deleteMember(Long id) {
        Member member = findById(id);
        return member.deleteMember(member);
    }

    private BooleanExpression likeName(String username) {
        if (StringUtils.hasText(username)) {
            return member.name.like("%" + username + "%");
        }
        return null;
    }

    public Optional<Member> findByKakaoId(String kakaoId) {
        try {
            Member member = em.createQuery("select m from Member m where m.kakaoId = :kakaoId", Member.class)
                    .setParameter("kakaoId", kakaoId)
                    .getSingleResult();
            return Optional.ofNullable(member);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
