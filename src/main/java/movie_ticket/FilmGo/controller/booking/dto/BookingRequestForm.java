package movie_ticket.FilmGo.controller.booking.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestForm {

    private Long movieId;

    private Long theaterId;
}
