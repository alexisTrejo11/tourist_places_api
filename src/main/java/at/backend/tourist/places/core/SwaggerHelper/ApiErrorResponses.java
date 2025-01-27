package at.backend.tourist.places.core.SwaggerHelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiErrorResponses {
    boolean notFound() default false;
    boolean unauthorized() default true;
    boolean forbidden() default true;
    boolean badRequest() default false;
}