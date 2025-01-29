package at.backend.tourist.places.core.Service;

import at.backend.tourist.places.core.Utils.DTOs.EmailSendingDTO;

public interface SendingService {
    void sendEmail(EmailSendingDTO emailSendingDTO);
}
