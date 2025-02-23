package movie_ticket.FilmGo.domain.theater;

import jakarta.persistence.*;
import lombok.*;
import movie_ticket.FilmGo.domain.member.Member;
import movie_ticket.FilmGo.domain.theater.enums.SeatStatus;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MovieSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_schedule_id")
    private MovieSchedule movieSchedule;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @Version
    private Long version;

    public MovieSeat(Seat seat, MovieSchedule movieSchedule, SeatStatus status) {
        this.seat = seat;
        this.movieSchedule = movieSchedule;
        this.status = status;
    }

    public void updateStatus(SeatStatus newStatus, Member member) {
        this.member = member;
        this.status = newStatus;
    }
}
