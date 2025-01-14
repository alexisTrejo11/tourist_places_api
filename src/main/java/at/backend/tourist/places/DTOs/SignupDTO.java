package at.backend.tourist.places.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupDTO {

    @NotBlank(message = "Username is required.")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters.")
    private String username;

    @JsonProperty("full_name")
    @NotBlank(message = "Full name is required.")
    @Size(max = 50, message = "Full name must not exceed 50 characters.")
    private String fullName;

    @NotNull(message = "Password is required.")
    @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters.")
    private String password;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be valid.")
    private String email;
}
