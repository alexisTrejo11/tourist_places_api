package at.backend.tourist.places.Service;

import at.backend.tourist.places.Utils.EmailSendingDTO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SendingServiceImpl implements SendingService{

    @Override
    @Async("taskExecutor")
    public void sendEmail(EmailSendingDTO emailSendingDTO) {
        switch (emailSendingDTO.getType()) {
            case RESET_PASSWORD_TOKEN -> sendResetPasswordTokenEmail(emailSendingDTO);
        }
    }

    private void sendResetPasswordTokenEmail(EmailSendingDTO emailSendingDTO) {

    }

}
