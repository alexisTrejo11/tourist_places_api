package at.backend.tourist.places.core.Utils.Response;


import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Result<T> {

    private final T value;
    private final String errorMessage;
    @Getter
    private final boolean success;

    private Result(T value, String errorMessage, boolean success) {
        this.value = value;
        this.errorMessage = errorMessage;
        this.success = success;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value, null, true);
    }

    public static <T> Result<T> success() {
        return new Result<>(null, null, true);
    }

    public static <T> Result<T> failure(String errorMessage) {
        return new Result<>(null, errorMessage, false);
    }

    public T getData() {
        return this.value;
    }


    public String getErrorMessage() {
        return !this.success ? this.errorMessage : "";
    }
}

