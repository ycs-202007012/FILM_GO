package movie_ticket.FilmGo.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ExChangeEntity {

    @AliasFor(annotation = Component.class)
    String value() default "";
}
