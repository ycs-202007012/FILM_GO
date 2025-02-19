package movie_ticket.FilmGo.domain.theater;

import jakarta.persistence.*;
import lombok.*;
import movie_ticket.FilmGo.controller.theater.dto.TheaterForm;
import movie_ticket.FilmGo.domain.Address;
import movie_ticket.FilmGo.domain.theater.enums.TheaterStatus;
import movie_ticket.FilmGo.domain.thmv.TheaterMovie;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Theater {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_id")
    private Long id;
    private String name;

    @OneToMany(fetch = LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "theater_movie_id")
    private List<TheaterMovie> theaterMovies = new ArrayList<>();

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private TheaterStatus status;

    @Builder.Default
    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TheaterHouse> theaterHouses = new ArrayList<>();

    public void updateTheater(TheaterForm form) {
        this.name = form.getName();
        this.address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
    }

    public void deleteTheater() {
        this.status = TheaterStatus.UNREGISTERED;
    }

    public void addTheaterMovie(TheaterMovie theaterMovie){
        this.theaterMovies.add(theaterMovie);
    }


}
