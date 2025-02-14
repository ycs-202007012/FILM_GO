package movie_ticket.FilmGo.controller.member.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberForm {

    @NotBlank
    private String name;
    @NotBlank
    private String password;
    @NotBlank
    private String username;

    @NotNull
    @Min(1) @Max(110)
    private Integer age;

    @NotBlank
    private String phoneNumber;

}
