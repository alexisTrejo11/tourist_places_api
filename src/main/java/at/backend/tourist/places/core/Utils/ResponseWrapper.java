package at.backend.tourist.places.core.Utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;


@Data
@NoArgsConstructor
@JsonRootName(value = "ResponseWrapper")
@Schema(description = "Wrapper class for standardized API responses")
public class ResponseWrapper<T> {

    @Schema(description = "Indicates if the operation was successful", example = "true")
    @JsonProperty("success")
    private boolean success;

    @Schema(description = "The response payload data")
    @JsonProperty("data")
    private T data;

    @Schema(description = "Response message providing additional information", example = "User successfully created")
    @JsonProperty("message")
    private String message;

    @Schema(description = "HTTP status code", example = "200")
    @JsonProperty("status_code")
    private int statusCode;

    // Constructor with same annotations
    public ResponseWrapper(
            @Schema(description = "Operation success status") boolean success,
            @Schema(description = "Response payload") T data,
            @Schema(description = "Response message") String message,
            @Schema(description = "HTTP status code") int statusCode
    ) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.statusCode = statusCode;
    }

    // Static factory methods with operation annotations
    @Schema(description = "Creates a success response for entity creation")
    public static <T> ResponseWrapper<T> created(String entity) {
        String createMsg = entity + " successfully created";
        return new ResponseWrapper<>(true, null, createMsg, 201);
    }

    @Schema(description = "Creates a success response for entity creation with data")
    public static <T> ResponseWrapper<T> created(T data, String entity) {
        String createMsg = entity + " successfully created";
        return new ResponseWrapper<>(true, data, createMsg, 201);
    }

    @Schema(description = "Creates a success response for found entities")
    public static <T> ResponseWrapper<T> found(T data, String entity) {
        String foundMsg = entity + " data successfully Fetched";
        return new ResponseWrapper<>(true, data, foundMsg, 200);
    }

    @Schema(description = "Creates a success response for general operations")
    public static <T> ResponseWrapper<T> ok(T data, String entity, String action) {
        String notFoundMsg = entity + " successfully " + action + "d";
        return new ResponseWrapper<>(true, data, notFoundMsg, 200);
    }

    @Schema(description = "Creates a success response without data")
    public static <T> ResponseWrapper<T> ok(String entity, String action) {
        String notFoundMsg = entity + " successfully " + action + "d";
        return new ResponseWrapper<>(true, null, notFoundMsg, 200);
    }

    @Schema(description = "Creates a success response for deletion")
    public static <T> ResponseWrapper<T> deleted(String entity) {
        String deleteMsg = entity + " successfully deleted";
        return new ResponseWrapper<>(true, null, deleteMsg, 204);
    }

    @Schema(description = "Creates a generic success response")
    public static <T> ResponseWrapper<T> success(String msg) {
        return new ResponseWrapper<>(true, null, msg, 200);
    }

    @Schema(description = "Creates a not found error response")
    public static <T> ResponseWrapper<T> notFound(String entity) {
        String notFoundMsg = entity + " not Found";
        return new ResponseWrapper<>(false, null, notFoundMsg, 404);
    }

    @Schema(description = "Creates a bad request error response")
    public static <T> ResponseWrapper<T> badRequest(String msg) {
        return new ResponseWrapper<>(false, null, msg, 400);
    }

    @Schema(description = "Creates an unauthorized error response")
    public static <T> ResponseWrapper<T> unauthorized(String msg) {
        return new ResponseWrapper<>(false, null, msg, 401);
    }

    @Schema(description = "Creates a custom error response")
    public static <T> ResponseWrapper<T> error(String msg, int statusCode) {
        return new ResponseWrapper<>(false, null, msg, statusCode);
    }
}