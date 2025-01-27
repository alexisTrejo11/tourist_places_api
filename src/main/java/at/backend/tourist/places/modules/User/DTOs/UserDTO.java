package at.backend.tourist.places.modules.User.DTOs;

import at.backend.tourist.places.core.Utils.Enum.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "User data transfer object")
public class UserDTO {

    @JsonProperty("id")
    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;

    @JsonProperty("name")
    @Schema(description = "Name of the user", example = "John Doe")
    private String name;

    @JsonProperty("email")
    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;

    @JsonProperty("role")
    @Schema(description = "Role of the user in the system", example = "ADMIN")
    private Role role;

    @Schema(description = "Session token for authentication")
    private String sessionToken;

    public UserDTO(String name, String email, String sessionToken) {
        this.name = name;
        this.email = email;
        this.sessionToken = sessionToken;
    }
}
