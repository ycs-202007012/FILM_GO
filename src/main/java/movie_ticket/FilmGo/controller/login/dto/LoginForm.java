package movie_ticket.FilmGo.controller.login.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {

    @NotBlank
    private String name;
    @NotBlank
    private String password;
}
