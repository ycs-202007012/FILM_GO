package movie_ticket.FilmGo.controller.theaterHouse.domain;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import movie_ticket.FilmGo.domain.theater.Theater;

import static jakarta.persistence.FetchType.LAZY;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TheaterHouseResponse {

    @NotNull
    private Long id;

    @NotBlank
    private String houseNumber;

    @NotNull
    private Long theaterId;

    @NotNull
    private Integer seat;
}
