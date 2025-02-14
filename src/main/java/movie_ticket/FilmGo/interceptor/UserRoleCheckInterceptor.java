package movie_ticket.FilmGo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.controller.login.LoginSession;
import movie_ticket.FilmGo.domain.member.enums.MemberRole;
import movie_ticket.FilmGo.domain.member.enums.MemberStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

import static movie_ticket.FilmGo.domain.member.enums.MemberRole.*;

@Slf4j
public class UserRoleCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(LoginSession.USER_ROLE)
                == null || session.getAttribute(LoginSession.LOG_ID) == null) {
            log.info("[{}] session이 존재하지 않습니다.", LocalDateTime.now());
            response.sendRedirect("/login?redirectURL=" + requestURI);
            return false;
        }
        MemberRole role = (MemberRole) session.getAttribute("userRole");

        if (!role.equals(MASTER)) {
            log.info("이 사이트를 들어올 권한이 없습니다.");
            response.sendRedirect("/");
            return false;
        }
        log.info("권한 인증 성공!!");
        return true;
    }
}
