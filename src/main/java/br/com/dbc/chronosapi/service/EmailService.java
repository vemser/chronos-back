package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.dto.usuario.UsuarioDTO;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailService {

    private final freemarker.template.Configuration fmConfiguration;

    @Value("${spring.mail.username}")
    private String from;


    private final JavaMailSender emailSender;

    public void sendEmailEnvioSenha(UsuarioDTO usuario, String senha) {
        String subject = "Sua conta foi criada com sucesso!";
        sendEmail(usuario, senha, "email-envio-senha-template.html", subject);
    }

    public void sendEmailRecuperacaoSenha(UsuarioDTO usuario, String token) {
        String subject = "Recuperação de senha concluída com sucesso!";
        String link = "recuperadorDeSenha@dbccompany.com.br";
        sendEmailRecover(usuario, token, link, "email-recuperacao-senha-template.html", subject);
    }

    public void sendEmail(UsuarioDTO usuario, String senha, String nomeTemplate, String assunto) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(usuario.getEmail());
            mimeMessageHelper.setSubject(assunto);
            mimeMessageHelper.setText(getContentFromTemplate(usuario, nomeTemplate, senha), true);
            emailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEmailRecover(UsuarioDTO usuario, String senha, String link, String nomeTemplate, String assunto) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(usuario.getEmail());
            mimeMessageHelper.setSubject(assunto);
            mimeMessageHelper.setText(getContentFromTemplateRecover(usuario, nomeTemplate, senha, link), true);
            emailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException | IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    public String getContentFromTemplate(UsuarioDTO usuario, String nomeTemplate, String senha) throws IOException, TemplateException {
        Map<String, Object> dados = new HashMap<>();
        dados.put("nome", usuario.getNome());
        dados.put("emailSuporte", from);
        dados.put("senha", senha);
        Template template = fmConfiguration.getTemplate(nomeTemplate);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
    }

    public String getContentFromTemplateRecover(UsuarioDTO usuario, String nomeTemplate, String token, String link) throws IOException, TemplateException {
        Map<String, Object> dados = new HashMap<>();
        dados.put("nome" , usuario.getNome());
        dados.put("emailSuporte", from);
        dados.put("token", token);
        dados.put("link", link);
        Template template = fmConfiguration.getTemplate(nomeTemplate);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
    }
}
