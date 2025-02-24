package movie_ticket.FilmGo.controller.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import movie_ticket.FilmGo.domain.member.Member;
import movie_ticket.FilmGo.domain.theater.MovieSchedule;
import movie_ticket.FilmGo.domain.theater.MovieSeat;
import movie_ticket.FilmGo.service.MovieSeatService;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestForm {

    @NotNull
    public int personCount;

    @NotNull
    public List<Long> seatIds;

    @NotNull
    private Long memberId;

    @NotNull
    private Integer totalPrice;

    private Integer chargeMoney;

}
