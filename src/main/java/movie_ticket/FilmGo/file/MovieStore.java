package movie_ticket.FilmGo.file;

import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.domain.movie.Movie;
import movie_ticket.FilmGo.domain.upload.MovieUploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class MovieStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    public List<MovieUploadFile> uploadFiles(List<MultipartFile> multipartFiles) {
        List<MovieUploadFile> movieUploadFiles = new ArrayList<>();
        if (multipartFiles.isEmpty()) {
            return null;
        }
        for (MultipartFile multipartFile : multipartFiles) {
            movieUploadFiles.add(storeFile(multipartFile));
        }

        return movieUploadFiles;
    }

    public MovieUploadFile storeFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String storeFileName = createStoreFileName(originalFilename);
            multipartFile.transferTo(new File(getFullPath(storeFileName)));

            log.info("IMAGE FILE 저장 성공!! 저장 위치:[{}]", getFullPath(storeFileName));

            return new MovieUploadFile(originalFilename, storeFileName);
        } catch (Exception e) {
            return null;
        }

    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        return UUID.randomUUID().toString() + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
