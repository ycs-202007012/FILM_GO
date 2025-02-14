package movie_ticket.FilmGo.domain.booking;

import jakarta.persistence.*;
import lombok.*;
import movie_ticket.FilmGo.domain.booking.enums.BookingStatus;
import movie_ticket.FilmGo.domain.member.Member;
import movie_ticket.FilmGo.domain.theater.Theater;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @Column(name = "booking_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "theater_id")
    private Theater theater;

    private LocalDateTime bookingDate;
    private int seatNumber;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
