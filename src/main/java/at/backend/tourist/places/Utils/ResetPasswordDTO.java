package at.backend.tourist.places.Utils;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ResetPasswordDTO {
    private String token;
    private String newPassword;
}
