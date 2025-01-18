package at.backend.tourist.places.DTOs;

import at.backend.tourist.places.Utils.Enum.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInsertDTO {
    @JsonProperty("name")
    private String name;

    @NotNull
    @NotBlank
    @JsonProperty("password")
    private String password;

    @NotNull
    @NotBlank
    @JsonProperty("email")
    private String email;

    @NotNull
    @JsonProperty("role")
    private Role role;


}
