package movie_ticket.FilmGo.domain.theater;

import jakarta.persistence.*;
import lombok.*;
import movie_ticket.FilmGo.controller.theaterHouse.domain.TheaterHouseForm;
import movie_ticket.FilmGo.domain.theater.enums.SeatStatus;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TheaterHouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_house_id")
    private Long id;

    private String houseName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "theater_id")
    private Theater theater;

    @OneToMany(mappedBy = "theaterHouse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "theaterHouse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieSchedule> movieSchedules = new ArrayList<>();

    public TheaterHouse(String houseName, Theater theater, Integer seatCount) {
        this.houseName = houseName;
        this.theater = theater;
        for (int i = 1; i <= seatCount; i++) {
            seats.add(new Seat(null, i, SeatStatus.AVAILABLE, this));
        }
    }

    public void addMovieSchedule(MovieSchedule movieSchedule) {
        this.getMovieSchedules().add(movieSchedule);
    }


    public void update(TheaterHouseForm form) {
        this.houseName = form.getHouseName();
    }

}
