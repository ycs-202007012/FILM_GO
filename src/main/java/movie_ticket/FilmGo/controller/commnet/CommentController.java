package movie_ticket.FilmGo.controller.commnet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.controller.commnet.dto.CommentForm;
import movie_ticket.FilmGo.controller.login.LoginSession;
import movie_ticket.FilmGo.converter.CommentConverter;
import movie_ticket.FilmGo.domain.comment.Comment;
import movie_ticket.FilmGo.domain.member.Member;
import movie_ticket.FilmGo.service.CommentService;
import movie_ticket.FilmGo.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static movie_ticket.FilmGo.controller.login.LoginSession.*;

@Slf4j
@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final MemberService memberService;
    private final CommentService commentService;
    private final CommentConverter commentConverter;

    @PostMapping("/add")
    public String addComment(@RequestParam Long movieId, @RequestParam String content, HttpServletRequest request) {
        log.info(" 댓글 추가 요청: movieId={}, content={}", movieId, content);

        HttpSession session = request.getSession(false);

        Member member = memberService.findByServletRequest(request);
        Comment entity = commentConverter.toEntity(movieId, content, member.getId());
        commentService.save(entity);

        return "redirect:/movies/" + movieId;
    }

    @PostMapping("/update/{commentId}")
    public String updateComment(@PathVariable Long commentId, @RequestParam String content, HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {
        Comment comment = commentService.findById(commentId);
        Long movieId = comment.getMovie().getId();
        HttpSession session = request.getSession(false);
        Long memberId = (Long) session.getAttribute(LOG_ID);

        if (!comment.getMember().getId().equals(memberId)) {
            redirectAttributes.addFlashAttribute("errorMessage", "삭제할 권한이 없습니다.");
            return "redirect:/movies/" + movieId;
        }

        comment.updateContent(content);
        commentService.save(comment);
        redirectAttributes.addFlashAttribute("successMessage", "댓글이 삭제되었습니다.");

        return "redirect:/movies/" + movieId;
    }


    @PostMapping("/delete/{commentId}")
    public String deleteComment(@PathVariable Long commentId, HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {
        log.info("댓글 작성자 ID:{}", commentId);
        Comment comment = commentService.findById(commentId);
        Long movieId = comment.getMovie().getId();
        HttpSession session = request.getSession(false);
        Long memberId = (Long) session.getAttribute(LOG_ID);

        if (!comment.getMember().getId().equals(memberId)) {
            redirectAttributes.addFlashAttribute("errorMessage", "삭제할 권한이 없습니다.");
            return "redirect:/movies/" + movieId;
        }

        comment.softDelete();
        commentService.save(comment);
        redirectAttributes.addFlashAttribute("successMessage", "댓글이 삭제되었습니다.");

        return "redirect:/movies/" + movieId;
    }


}
