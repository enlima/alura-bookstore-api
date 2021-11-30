package br.com.alura.bookstore.infra.email;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Profile({"default", "test"})
public class EmailServiceDev implements EmailService {

    @Override
    @Async
    public void sendEmail(String to, String subject, String message) {

        System.out.println("=== Sending email... ===");
        System.out.println("=== To: " + to);
        System.out.println("=== Subject: " + subject);
        System.out.println("=== Message: " + message);
        System.out.println("========================");
    }
}
