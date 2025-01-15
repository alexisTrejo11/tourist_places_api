package at.backend.tourist.places.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDTO {

    @NotNull(message = "email can't be null. Valid fields: username or email")
    @NotBlank(message = "email is blank. Valid fields: username or email")
    private String email;

    @NotNull(message = "Password is required.")
    private String password;

}
