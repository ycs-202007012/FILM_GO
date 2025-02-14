package movie_ticket.FilmGo.controller.commnet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.w3c.dom.Text;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentForm {

    @NotNull
    private Long movieId;

    @NotBlank
    private String content;

}
