package movie_ticket.FilmGo.domain.theater;

import jakarta.persistence.*;
import lombok.*;
import movie_ticket.FilmGo.domain.theater.enums.SeatStatus;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@BatchSize(size = 50)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int seatNumber;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_house_id")
    private TheaterHouse theaterHouse;
}
