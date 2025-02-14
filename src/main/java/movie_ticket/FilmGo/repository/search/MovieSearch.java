package movie_ticket.FilmGo.repository.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movie_ticket.FilmGo.domain.movie.enums.MovieStatus;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieSearch {

    private String title;
    private MovieStatus status;
}
