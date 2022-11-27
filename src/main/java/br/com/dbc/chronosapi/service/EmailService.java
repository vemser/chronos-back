package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.entity.classes.UsuarioEntity;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    private static final String TO = "luiz.martins@dbccompany.com.br";

    private final JavaMailSender emailSender;

    public void sendRecoverPasswordEmail(UsuarioEntity usuarioEntity, String assunto, String templateTipo) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(usuarioEntity.getEmail());
            mimeMessageHelper.setSubject(assunto);
            mimeMessageHelper.setText(getContentFromTemplate(usuarioEntity, templateTipo), true);

            emailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException | IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    public String getContentFromTemplate(UsuarioEntity usuarioEntity, String templateTipo) throws IOException, TemplateException {
        Map<String, Object> dados = new HashMap<>();
        dados.put("nomeUsuario", usuarioEntity.getNome());
        dados.put("idUsuario", usuarioEntity.getIdUsuario());
        dados.put("emailSuporte", from);


        Template template = fmConfiguration.getTemplate(templateTipo);
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
        return html;
    }
}
