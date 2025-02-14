package movie_ticket.FilmGo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movie_ticket.FilmGo.controller.login.LoginSession;
import movie_ticket.FilmGo.domain.member.enums.MemberRole;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RequiredArgsConstructor
public class LoginInfoInterceptor implements HandlerInterceptor {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && !isRedirectView(modelAndView)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                Long memberId = (Long) session.getAttribute(LoginSession.LOG_ID);
                modelAndView.addObject("IS_LOGGED_IN", true);
                modelAndView.addObject("USER_NAME", session.getAttribute("USER_NAME"));
                modelAndView.addObject("IS_MASTER", session.getAttribute(LoginSession.USER_ROLE).equals(MemberRole.MASTER));
                log.info("로그인한 유저");
            } else {
                modelAndView.addObject("IS_LOGGED_IN", false);
                log.info("로그인 X");
            }
        }
    }
    private boolean isRedirectView(ModelAndView modelAndView) {
        String viewName = modelAndView.getViewName();
        return viewName != null && viewName.startsWith("redirect:");
    }
}
