package movie_ticket.FilmGo.domain.member;

import jakarta.persistence.*;
import lombok.*;
import movie_ticket.FilmGo.domain.member.enums.MemberRole;
import movie_ticket.FilmGo.domain.member.enums.MemberStatus;
import movie_ticket.FilmGo.domain.member.enums.SocialType;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String kakaoId; //kakao Login Id

    @Column(unique = true)
    private String name;
    private String password;
    private String username;  //kakao는 nickname 가져옴
    private Integer age;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;
    private String phoneNumber;

    public static Member createKakaoMember(String kakaoId, String username) {
        return Member.builder()
                .kakaoId(kakaoId)
                .username(username)
                .socialType(SocialType.KAKAO)
                .role(MemberRole.USER)  // 기본 권한 설정
                .build();
    }

    public Member deleteMember(Member member) {
        this.status = MemberStatus.UNREGISTERED;
        return this;
    }
}
