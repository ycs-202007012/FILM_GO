package movie_ticket.FilmGo.controller.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestForm {

    @NotNull
    private Long movieId;

    @NotNull
    private List<Integer> seatIds;
}
