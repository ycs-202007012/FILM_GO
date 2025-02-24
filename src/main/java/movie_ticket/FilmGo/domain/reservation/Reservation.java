package movie_ticket.FilmGo.domain.reservation;

import jakarta.persistence.*;
import lombok.*;
import movie_ticket.FilmGo.domain.member.Member;
import movie_ticket.FilmGo.domain.movie.Movie;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_Id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private Integer totalPrice;

    private LocalDateTime reservationTime;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    public static Reservation createReservation(Member member, Movie movie, int totalPrice) {
        return Reservation.builder()
                .member(member)
                .movie(movie)
                .totalPrice(totalPrice)
                .status(ReservationStatus.RESERVATION)
                .reservationTime(LocalDateTime.now()).build();
    }
}
