package movie_ticket.FilmGo.domain.theater;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public class StartEndTime {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
