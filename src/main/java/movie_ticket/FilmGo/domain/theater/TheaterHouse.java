package movie_ticket.FilmGo.domain.theater;

import jakarta.persistence.*;
import lombok.*;
import movie_ticket.FilmGo.controller.theaterHouse.domain.TheaterHouseForm;
import movie_ticket.FilmGo.exception.NotEnoughStockException;

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

    private String houseNumber;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "theater_id")
    private Theater theater;

    private Integer seat;

    @Builder.Default
    @OneToMany(mappedBy = "theaterHouse", cascade = CascadeType.ALL)
    private List<MovieSchedule> movieSchedules = new ArrayList<>();

    public TheaterHouse(String houseNumber, Theater theater, Integer seat) {
        this.houseNumber = houseNumber;
        this.theater = theater;
        this.seat = seat;
    }

    public void addMovieSchedule(MovieSchedule movieSchedule) {
        this.getMovieSchedules().add(movieSchedule);
    }

    public void addSeat(int stock) {
        this.seat += stock;
    }

    public void removeSeat(int stock) {
        int restStock = this.seat - stock; // 변경된 좌석 수 계산
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.seat = restStock; // 계산된 값을 최종적으로 업데이트
    }


    public void update(TheaterHouseForm form) {
        this.houseNumber = form.getHouseNumber();
        this.seat = form.getSeat();
    }

}
