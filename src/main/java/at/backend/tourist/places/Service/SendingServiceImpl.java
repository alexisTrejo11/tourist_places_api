package at.backend.tourist.places.Service;

import at.backend.tourist.places.Utils.EmailSendingDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Slf4j
@Service
public class SendingServiceImpl implements SendingService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    @Async("taskExecutor")
    public void sendEmail(EmailSendingDTO emailSendingDTO) {
        switch (emailSendingDTO.getType()) {
            case RESET_PASSWORD_TOKEN -> sendResetPasswordTokenEmail(emailSendingDTO);
        }
    }

    private void sendResetPasswordTokenEmail(EmailSendingDTO emailSendingDTO) {
        Context context = new Context();
        context.setVariable("resetCode", emailSendingDTO.getToken());

        String htmlContent = templateEngine.process("reset-password-email", context);

        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setTo(emailSendingDTO.getEmail());
            messageHelper.setSubject("Password Recovery Request");
            messageHelper.setText(htmlContent, true);
        };

        mailSender.send(preparator);
        log.info("Reset password email successfully send to {}", emailSendingDTO.getEmail());
    }
}
