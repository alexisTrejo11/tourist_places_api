package at.backend.tourist.places.modules.Auth.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignupDTO {
    @JsonProperty("name")
    @NotBlank(message = "name is required.")
    @Size(max = 50, message = "name must not exceed 50 characters.")
    private String name;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be valid.")
    private String email;

    @NotNull(message = "Password is required.")
    @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters.")
    private String password;
}
