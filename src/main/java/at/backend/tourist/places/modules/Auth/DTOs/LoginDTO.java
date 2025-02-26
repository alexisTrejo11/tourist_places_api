package at.backend.tourist.places.modules.Auth.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDTO {

    @NotNull(message = "email can't be null. Valid fields: username or email")
    @NotBlank(message = "email is blank. Valid fields: username or email")
    private String email;

    @NotNull(message = "Password is required.")
    private String password;

}
