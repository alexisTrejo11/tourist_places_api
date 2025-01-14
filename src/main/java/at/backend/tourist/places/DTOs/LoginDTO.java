package at.backend.tourist.places.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDTO {

    @JsonProperty("identifier_field")
    @NotNull(message = "identifier_field can't be null. Valid fields: username or email")
    @NotBlank(message = "identifier_field is blank. Valid fields: username or email")
    private String identifierField;

    @NotNull(message = "Password is required.")
    private String password;

}
