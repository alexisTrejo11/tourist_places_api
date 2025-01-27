package at.backend.tourist.places.core.SwaggerHelper;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class ApiResponseAspect {

    @Around("@annotation(apiSuccessResponse)")
    public Object handleApiResponse(ProceedingJoinPoint joinPoint, ApiSuccessResponse apiSuccessResponse) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        return joinPoint.proceed();
    }

    private String getEntityName(Method method) {
        ApiSuccessResponse annotation = AnnotationUtils.findAnnotation(method, ApiSuccessResponse.class);
        if (annotation != null) {
            return annotation.dataType().getSimpleName();
        }
        return "Entity";
    }
}