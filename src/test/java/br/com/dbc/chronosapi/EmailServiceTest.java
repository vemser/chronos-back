//package br.com.dbc.chronosapi;
//
//import br.com.dbc.chronosapi.dto.edicao.EdicaoCreateDTO;
//import br.com.dbc.chronosapi.dto.etapa.EtapaDTO;
//import br.com.dbc.chronosapi.dto.usuario.LoginDTO;
//import br.com.dbc.chronosapi.dto.usuario.UsuarioDTO;
//import br.com.dbc.chronosapi.entity.classes.EdicaoEntity;
//import br.com.dbc.chronosapi.entity.classes.EtapaEntity;
//import br.com.dbc.chronosapi.entity.classes.UsuarioEntity;
//import br.com.dbc.chronosapi.entity.classes.processos.AreaEnvolvidaEntity;
//import br.com.dbc.chronosapi.entity.classes.processos.ProcessoEntity;
//import br.com.dbc.chronosapi.entity.classes.processos.ResponsavelEntity;
//import br.com.dbc.chronosapi.entity.enums.Status;
//import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
//import br.com.dbc.chronosapi.service.EmailService;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import freemarker.template.Template;
//import freemarker.template.TemplateException;
//import org.junit.Before;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
//
//import javax.mail.internet.MimeMessage;
//import java.io.IOException;
//import java.io.Reader;
//import java.time.LocalDate;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class EmailServiceTest {
//
//    @InjectMocks
//    private EmailService emailService;
//
//    @Value("${spring.mail.username}")
//    private String from;
//
//    @Mock
//    private freemarker.template.Configuration fmConfiguration;
//
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    @Before
//    public void init() {
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//    }
//
//    @Test
//    public void testSendRecoverPasswordEmail(){
//
//    }
//
////    @Test
////    public void deveTestarGetContentFromTemplate() throws IOException, TemplateException, RegraDeNegocioException {
////
////        // SETUP
////        Template template = new Template("", Reader.nullReader());
////        UsuarioEntity usuarioEntity = getUsuarioEntity();
////        Map<String, Object> dados = new HashMap<>();
////        dados.put("nomeUsuario", usuarioEntity.getNome());
////        dados.put("senha", usuarioEntity.getSenha());
////        dados.put("emailSuporte", from);
////
////        when(fmConfiguration.getTemplate(anyString())).thenReturn(template);
////
////        // ACT
////        String getContent = emailService.getContentFromTemplate(usuarioEntity, usuarioEntity.getSenha(), "email-recuperacao-senha-template.ftl");
////        template = fmConfiguration.getTemplate("email-recuperacao-senha-template.ftl");
////        FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
////
////        // ASSERT
////        assertNotNull(getContent);
////
////    }
//
//    @Test
//    public void deveTestarGeContentFromTemplate() throws TemplateException, IOException {
//
//        // Criar variaveis (SETUP)
//
//        Template template = new Template("", Reader.nullReader());
//        UsuarioEntity usuarioEntity = new UsuarioEntity();
//        usuarioEntity.setIdUsuario(1);
//        usuarioEntity.setNome("alison");
//        usuarioEntity.setEmail("alison@hotmail.com");
//        usuarioEntity.setSenha("123123");
//        Map<String, Object> dados = new HashMap<>();
//        dados.put("nome", usuarioEntity.getNome());
//        dados.put("id", usuarioEntity.getIdUsuario());
//        dados.put("emailUsuario", usuarioEntity.getEmail());
//        dados.put("email", from);
//
//        when(fmConfiguration.getTemplate(anyString())).thenReturn(template);
//
//        // Ação (ACT)
//        String geContentFromTemplateUsuario = emailService.getContentFromTemplate(usuarioEntity, "123123", "email-recuperacao-senha-template.ftl");
//        template = fmConfiguration.getTemplate("emailcreate-template.html");
//        FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
//
//        // Verificação (ASSERT)
//        assertNotNull(geContentFromTemplateUsuario);
//
//    }
//
//
//    private static UsuarioEntity getUsuarioEntity() {
//        UsuarioEntity usuarioEntity = new UsuarioEntity();
//        usuarioEntity.setIdUsuario(1);
//        usuarioEntity.setNome("Luiz Martins");
//        usuarioEntity.setEmail("luiz@gemail.com");
//        usuarioEntity.setSenha("12346789");
//        usuarioEntity.setImagem(null);
//        usuarioEntity.setStatus(Status.ATIVO);
//        usuarioEntity.setCargos(new HashSet<>());
//
//        return usuarioEntity;
//    }
//
//    private static UsuarioDTO getUsuarioDTO() {
//        UsuarioDTO usuarioDTO = new UsuarioDTO();
//        usuarioDTO.setIdUsuario(1);
//        usuarioDTO.setNome("Luiz Martins");
//        usuarioDTO.setEmail("luiz@gemail.com");
//        usuarioDTO.setImagem(null);
//        usuarioDTO.setStatus(Status.ATIVO);
//        usuarioDTO.setCargos(new HashSet<>());
//
//        return usuarioDTO;
//    }
//    private EtapaDTO getEtapaDTO() {
//        EtapaDTO etapaDTO = new EtapaDTO();
//        etapaDTO.setIdEtapa(10);
//        etapaDTO.setOrdemExecucao(2);
//        etapaDTO.setNome("Etapa1");
//
//        etapaDTO.setProcessos(new HashSet<>());
//
//
//        return etapaDTO;
//    }
//
//    private LoginDTO getLoginDTO() {
//        LoginDTO loginDTO = new LoginDTO();
//        loginDTO.setEmail("pasap@gmail.com");
//        loginDTO.setSenha("123123");
//
//
//        return loginDTO;
//    }
//
//    private EdicaoCreateDTO getEdicaoCreateDTO() {
//        EdicaoCreateDTO edicaoCreateDTO = new EdicaoCreateDTO();
//        edicaoCreateDTO.setNome("Edicao1");
//        edicaoCreateDTO.setDataInicial(LocalDate.of(2022,8,1));
//        edicaoCreateDTO.setDataFinal(LocalDate.of(2022,8,10));
//
//        return edicaoCreateDTO;
//    }
//    private static EdicaoEntity getEdicaoEntity() {
//
//        EdicaoEntity edicaoEntity = new EdicaoEntity();
//        edicaoEntity.setIdEdicao(10);
//        edicaoEntity.setNome("Edicao1");
//        edicaoEntity.setDataInicial(LocalDate.of(2022, 10, 11));
//        edicaoEntity.setDataFinal(LocalDate.of(2022, 12, 10));
//        edicaoEntity.setEtapas(new HashSet<>());
//
//        return edicaoEntity;
//    }
//
//    private static EtapaEntity getEtapaEntity() {
//        EtapaEntity etapaEntity = new EtapaEntity();
//        etapaEntity.setIdEtapa(2);
//        etapaEntity.setEdicao(getEdicaoEntity());
//        etapaEntity.setNome("Etapa1");
//
//        Set<ProcessoEntity> processoEntities = new HashSet<>();
//        processoEntities.add(getProcessoEntity());
//        etapaEntity.setProcessos(processoEntities);
//
//        return etapaEntity;
//    }
//
//    private static ProcessoEntity getProcessoEntity() {
//        ProcessoEntity processoEntity = new ProcessoEntity();
//        processoEntity.setIdProcesso(10);
//        processoEntity.setDuracaoProcesso("1dia");
//        processoEntity.setEtapa(getEtapaEntity());
//        processoEntity.setOrdemExecucao(1);
//        processoEntity.setDiasUteis(1);
//        processoEntity.setAreasEnvolvidas(new HashSet<>());
//        processoEntity.setResponsaveis(new HashSet<>());
//
//        return processoEntity;
//    }
//
//    private static ResponsavelEntity getResponsavelEntity() {
//        ResponsavelEntity responsavelEntity = new ResponsavelEntity();
//        responsavelEntity.setIdResponsavel(10);
//        responsavelEntity.setNome("Fulano");
//
//        Set<ProcessoEntity> processoEntities = new HashSet<>();
//        processoEntities.add(getProcessoEntity());
//        responsavelEntity.setProcessos(processoEntities);
//
//        return responsavelEntity;
//    }
//
//    private static AreaEnvolvidaEntity getAreaEnvolvida() {
//        AreaEnvolvidaEntity areaEnvolvidaEntity = new AreaEnvolvidaEntity();
//        areaEnvolvidaEntity.setNome("Area1");
//        areaEnvolvidaEntity.setIdAreaEnvolvida(10);
//
//        Set<ProcessoEntity> processoEntities = new HashSet<>();
//        processoEntities.add(getProcessoEntity());
//        areaEnvolvidaEntity.setProcessos(processoEntities);
//
//        return areaEnvolvidaEntity;
//    }
//
//}
//
