package movie_ticket.FilmGo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.controller.login.LoginSession;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(LoginSession.USER_ROLE)
                == null || session.getAttribute(LoginSession.LOG_ID) == null) {
            log.info("session 인증 실패");
            response.sendRedirect("/login?redirectURL=" + requestURI);

            return false;
        }

        log.info("session 인증 성공");
        return true;
    }
}
