package at.backend.tourist.places.core.Utils.SwaggerHelper;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = ApiConstants.ACTIVITY_RETRIEVED),
        @ApiResponse(responseCode = "404", description = ApiConstants.ACTIVITY_NOT_FOUND)
})
public @interface CommonActivityResponses {
}