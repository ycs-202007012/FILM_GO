package movie_ticket.FilmGo.controller.movie.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import movie_ticket.FilmGo.domain.theater.Theater;
import movie_ticket.FilmGo.domain.upload.MovieUploadFile;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MovieForm {

    @NotBlank
    private String title;
    @NotNull
    private Integer time;
    @NotBlank
    private String director;

    @NotNull
    private Integer price;

    private String synopsis;

    @NotNull
    private LocalDate releaseDate;
    @NotNull
    private List<Long> theaterIds = new ArrayList<>();

    private MultipartFile movieUploadFile;
    private List<MultipartFile> movieUploadFiles;
}
