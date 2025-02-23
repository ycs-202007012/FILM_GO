package movie_ticket.FilmGo.domain.theater;

import jakarta.persistence.*;
import lombok.*;
import movie_ticket.FilmGo.domain.theater.enums.SeatStatus;
import org.hibernate.annotations.BatchSize;

import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_house_id")
    private TheaterHouse theaterHouse;

    public Seat(int seatNumber, TheaterHouse theaterHouse) {
        this.seatNumber = seatNumber;
        this.theaterHouse = theaterHouse;
    }
}
