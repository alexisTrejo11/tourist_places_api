package at.backend.tourist.places.Utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ResetPasswordDTO {
    @NotNull(message = "Token can't be null")
    @NotBlank(message = "Token can't be blank")
    private String token;

    @NotNull(message = "new_password can't be null")
    @NotBlank(message = "new_password can't be blank")
    @JsonProperty("new_password")
    private String newPassword;
}
