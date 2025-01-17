package at.backend.tourist.places.Service;

import at.backend.tourist.places.Utils.EmailSendingDTO;

public interface SendingService {
    void sendEmail(EmailSendingDTO emailSendingDTO);
}
