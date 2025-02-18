package movie_ticket.FilmGo.controller.theaterHouse.domain;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import movie_ticket.FilmGo.domain.theater.Theater;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TheaterHouseForm {

    @NotBlank
    @Size(min = 1, max = 2)
    private String houseName;

    @NotNull
    private Long theaterId;

    @NotNull
    private Integer seat;
}
