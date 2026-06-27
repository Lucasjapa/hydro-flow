package br.com.project.hydroflow.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final String from;

    public EmailService(JavaMailSender mailSender, @Value("${app.mail.from}") String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    public void sendPasswordResetEmail(String to, String token) {
        log.info("Enviando e-mail de recuperação de senha para: {}", to);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Recuperação de senha - Hydro Flow");
        message.setText(
                """
                Olá,

                Recebemos uma solicitação para redefinir sua senha.

                Use o token abaixo para concluir a recuperação:

                %s

                Este token expira em 1 hora.

                Se você não solicitou a redefinição, ignore este e-mail.
                """
                        .formatted(token));

        mailSender.send(message);
        log.info("E-mail de recuperação de senha enviado para: {}", to);
    }
}
