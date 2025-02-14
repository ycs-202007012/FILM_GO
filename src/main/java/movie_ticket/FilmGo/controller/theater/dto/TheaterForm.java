package movie_ticket.FilmGo.controller.theater.dto;

import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import movie_ticket.FilmGo.domain.Address;
import movie_ticket.FilmGo.domain.theater.enums.TheaterStatus;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TheaterForm {

    private String name;
    private String city;
    private String street;
    private String zipcode;

}
