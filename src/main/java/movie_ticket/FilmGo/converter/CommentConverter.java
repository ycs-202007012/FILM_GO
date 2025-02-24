package movie_ticket.FilmGo.converter;

import lombok.RequiredArgsConstructor;
import movie_ticket.FilmGo.annotation.ExChangeEntity;
import movie_ticket.FilmGo.controller.commnet.dto.CommentForm;
import movie_ticket.FilmGo.domain.comment.Comment;
import movie_ticket.FilmGo.domain.comment.enums.CommentStatus;
import movie_ticket.FilmGo.domain.member.Member;
import movie_ticket.FilmGo.domain.movie.Movie;
import movie_ticket.FilmGo.service.MemberService;
import movie_ticket.FilmGo.service.MovieService;

@ExChangeEntity
@RequiredArgsConstructor
public class CommentConverter {

    private final MovieService movieService;
    private final MemberService memberService;

    public Comment toEntity(Long movieId, String content, Long memberId) {
        Movie movie = movieService.findById(movieId).orElseThrow(()
                -> new IllegalArgumentException("해당 영화를 찾을 수 없습니다. ID:" + movieId));
        Member member = memberService.findById(memberId);
        return Comment.builder()
                .content(content)
                .member(member)
                .movie(movie)
                .status(CommentStatus.ACTIVE)
                .build();
    }
}
