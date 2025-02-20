package movie_ticket.FilmGo.domain.theater.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SeatStatus {
    AVAILABLE, // 예약x
    PENDING,  //에약 중
    RESERVED; //예약 완료
}
