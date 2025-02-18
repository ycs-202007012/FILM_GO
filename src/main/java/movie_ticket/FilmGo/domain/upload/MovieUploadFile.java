package movie_ticket.FilmGo.domain.upload;

import jakarta.persistence.*;
import lombok.*;
import movie_ticket.FilmGo.domain.movie.Movie;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MovieUploadFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_upload_file_id")
    private Long id;

    private String uploadFileName;
    private String storeFileName;

    @Setter
    @ManyToOne(fetch = LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    public MovieUploadFile(String uploadFileName, String storeFIleName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFIleName;
    }

    public void updateMovieUploadFiles(){

    }
}
