package br.com.alura.bookstore.infra.email;

public interface EmailService {

    void sendEmail(String to, String subject, String message);
}
