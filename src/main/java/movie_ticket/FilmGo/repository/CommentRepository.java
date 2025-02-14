package movie_ticket.FilmGo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.domain.comment.Comment;
import movie_ticket.FilmGo.domain.comment.QComment;
import movie_ticket.FilmGo.domain.comment.enums.CommentStatus;
import movie_ticket.FilmGo.domain.movie.Movie;
import movie_ticket.FilmGo.domain.movie.QMovie;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static movie_ticket.FilmGo.domain.comment.QComment.*;
import static movie_ticket.FilmGo.domain.member.QMember.member;
import static movie_ticket.FilmGo.domain.movie.QMovie.movie;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public Long save(Comment comment) {
        em.persist(comment);
        return comment.getId();
    }

    public Comment findById(Long id) {
        return em.find(Comment.class, id);
    }

    public List<Comment> findAll(Movie movie) {
        return query.select(comment)
                .from(comment)
                .where(comment.movie.eq(movie), comment.status.eq(CommentStatus.ACTIVE))
                .fetch();
    }
}
