package at.backend.tourist.places.Utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailSendingDTO {
    private String email;
    private String token;
    private SendingType type;

    public static EmailSendingDTO generatePasswordTokenDTO(String email, String token) {
      return new EmailSendingDTO(email,token, SendingType.RESET_PASSWORD_TOKEN);
    };

    public enum SendingType {
        RESET_PASSWORD_TOKEN,
    }
}
