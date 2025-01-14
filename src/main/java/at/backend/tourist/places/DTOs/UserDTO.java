package at.backend.tourist.places.DTOs;

import at.backend.tourist.places.Utils.Enum.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("role")
    private Role role;

    private String sessionToken;

    public UserDTO(String name, String email, String sessionToken) {
        this.name = name;
        this.email = email;
        this.sessionToken = sessionToken;
    }
}
