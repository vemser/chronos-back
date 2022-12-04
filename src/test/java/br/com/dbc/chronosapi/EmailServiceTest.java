package br.com.dbc.chronosapi;

import br.com.dbc.chronosapi.dto.usuario.UsuarioDTO;
import br.com.dbc.chronosapi.entity.classes.UsuarioEntity;
import br.com.dbc.chronosapi.entity.enums.Status;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.service.EmailService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {
    @InjectMocks
    private EmailService emailService;
    @Mock
    private freemarker.template.Configuration fmConfiguration;
    @Mock
    private JavaMailSender emailSender;
    @Mock
    private MimeMessage mimeMessage;
    private String from = "fromteste@email.com.br";

    @Before
    public void init() {
        ReflectionTestUtils.setField(emailService, "from", from);
    }


    @Test
    public void deveEnviarEmailComASenha() throws IOException, RegraDeNegocioException {
        Template template = new Template("email-envio-senha-template.html", Reader.nullReader());
        final String senha = "123";
        UsuarioDTO usuarioDTO = getUsuarioDTO();
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(fmConfiguration.getTemplate(any())).thenReturn(template);

        emailService.sendEmailEnvioSenha(usuarioDTO, senha);

        verify(emailSender).send((MimeMessage) any());
    }

    @Test
    public void deveEnviarEmailDeRecuperacaoDeSenha() throws IOException, RegraDeNegocioException {
        Template template = new Template("email-recuperacao-senha-template.html", Reader.nullReader());
        final String token = "$123token";
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(fmConfiguration.getTemplate(any())).thenReturn(template);
        UsuarioDTO usuarioDTO = getUsuarioDTO();

        emailService.sendEmailRecuperacaoSenha(usuarioDTO, token);

        verify(emailSender).send((MimeMessage) any());
    }

    @Test
    public void deveRetornarUmaExcecaoQuandoOcorrerUmErroNoEnvioDoEmail() throws IOException, MessagingException, TemplateException {
        Template template = new Template("email-envio-senha-funciona-pfv.html", Reader.nullReader());
        final String senha = "$123senha";
        final String nomeTemplate = "email-envio-senha-template.html";
        final String assunto = "Recuperação de senha concluída com sucesso.";
        UsuarioDTO usuarioDTO = getUsuarioDTO();
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new IOException()).when(fmConfiguration).getTemplate(anyString());

        emailService.sendEmail(usuarioDTO, senha, nomeTemplate, assunto);
    }

    @Test
    public void deveRetornarUmaExcecaoQuandoOcorrerUmErroNoEnvioDoEmailRecover() throws IOException {
        Template template = new Template("email-recuperacao-senhaaaaa.html", Reader.nullReader());
        UsuarioDTO usuarioDTO = getUsuarioDTO();
        final String senha = "$123senha";
        final String link = "google.com";
        final String nomeTemplate = "email-envio-senha-template.html";
        final String assunto = "Recuperação de senha concluída com sucesso.";
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new IOException()).when(fmConfiguration).getTemplate(anyString());

        emailService.sendEmailRecover(usuarioDTO, senha, link, nomeTemplate, assunto);
    }

    private static UsuarioEntity getUsuarioEntity() {
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setIdUsuario(1);
        usuarioEntity.setNome("Gustavo Linck");
        usuarioEntity.setEmail("linck@gemail.com");
        usuarioEntity.setSenha("12345");
        usuarioEntity.setStatus(Status.ATIVO);
        usuarioEntity.setCargos(new HashSet<>());

        return usuarioEntity;
    }

    private static UsuarioDTO getUsuarioDTO() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNome("Luiz");
        usuarioDTO.setIdUsuario(10);
        usuarioDTO.setNome("fulano");
        usuarioDTO.setEmail("fulano@gmaiul.com");
        usuarioDTO.setStatus(Status.ATIVO);
        usuarioDTO.setCargos(new HashSet<>());

        return usuarioDTO;
    }

}

