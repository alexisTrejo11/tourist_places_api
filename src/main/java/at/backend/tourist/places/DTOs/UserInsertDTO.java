package at.backend.tourist.places.DTOs;

import at.backend.tourist.places.Utils.Enum.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data transfer object for user creation (insert)")
public class UserInsertDTO {

    @JsonProperty("name")
    @Schema(description = "Name of the user", example = "John Doe")
    private String name;

    @NotNull
    @NotBlank
    @JsonProperty("password")
    @Schema(description = "Password for the user", example = "strongPassword123")
    private String password;

    @NotNull
    @NotBlank
    @JsonProperty("email")
    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;

    @NotNull
    @JsonProperty("role")
    @Schema(description = "Role assigned to the user", example = "ADMIN", allowableValues = {"ADMIN", "EDITOR", "VIEWER"})
    private Role role;

}
