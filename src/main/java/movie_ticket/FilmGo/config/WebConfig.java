package movie_ticket.FilmGo.config;

import lombok.RequiredArgsConstructor;
import movie_ticket.FilmGo.controller.theater.dto.TheaterForm;
import movie_ticket.FilmGo.domain.Address;
import movie_ticket.FilmGo.domain.member.Member;
import movie_ticket.FilmGo.domain.member.enums.MemberRole;
import movie_ticket.FilmGo.domain.member.enums.MemberStatus;
import movie_ticket.FilmGo.domain.theater.Theater;
import movie_ticket.FilmGo.domain.theater.enums.TheaterStatus;
import movie_ticket.FilmGo.interceptor.LogInterceptor;
import movie_ticket.FilmGo.interceptor.LoginInfoInterceptor;
import movie_ticket.FilmGo.interceptor.UserRoleCheckInterceptor;
import movie_ticket.FilmGo.service.MemberService;
import movie_ticket.FilmGo.service.TheaterHouseService;
import movie_ticket.FilmGo.service.TheaterService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final MemberService memberService;
    private final TheaterService theaterService;
    private final PasswordEncoder passwordEncoder;
    private final TheaterHouseService theaterHouseService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/path/to/**", "/images/**");
        /*registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/", "/members/new", "/login", "/path/to/**"
                        , "/images/**", "/login?logout", "/theaters/**", "/theater/**");*/
        registry.addInterceptor(new UserRoleCheckInterceptor())
                .order(3)
                .addPathPatterns("/theater/new", "/theaters/new", "/theater/edit", "/theaters/edit", "/theater-house/create/**")
                .excludePathPatterns();
        registry.addInterceptor(new LoginInfoInterceptor())
                .order(4)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error", "/path/to/**", "/images/**");
    }

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            memberService.save(Member.builder()
                    .name("kdj1785k@naver.com")
                    .password(passwordEncoder.encode("tnemforhs1!"))
                    .username("김수용")
                    .age(24)
                    .phoneNumber("010-2763-1785")
                    .role(MemberRole.MASTER)
                    .status(MemberStatus.REGISTERED)
                    .build());
        };
    }

    @Bean
    public CommandLineRunner initData2() {
        return args -> {
            theaterService.save(Theater.builder()
                    .name("김수용")
                    .address(new Address("경기도", "광명시", "14112"))
                    .status(TheaterStatus.REGISTERED)
                    .build());
        };
    }
}
