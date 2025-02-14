package movie_ticket.FilmGo.repository.search;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movie_ticket.FilmGo.domain.theater.enums.TheaterStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TheaterSearch {

    private String name;
    private TheaterStatus status;
}
