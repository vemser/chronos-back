package br.com.dbc.chronosapi;

import br.com.dbc.chronosapi.dto.edicao.EdicaoCreateDTO;
import br.com.dbc.chronosapi.dto.etapa.EtapaDTO;
import br.com.dbc.chronosapi.dto.usuario.LoginDTO;
import br.com.dbc.chronosapi.entity.classes.EdicaoEntity;
import br.com.dbc.chronosapi.entity.classes.EtapaEntity;
import br.com.dbc.chronosapi.entity.classes.UsuarioEntity;
import br.com.dbc.chronosapi.entity.classes.processos.AreaEnvolvidaEntity;
import br.com.dbc.chronosapi.entity.classes.processos.ProcessoEntity;
import br.com.dbc.chronosapi.entity.classes.processos.ResponsavelEntity;
import br.com.dbc.chronosapi.entity.enums.Status;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.UsuarioRepository;
import br.com.dbc.chronosapi.service.EmailService;
import br.com.dbc.chronosapi.service.LoginService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EmailService emailService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(loginService, "objectMapper", objectMapper);
    }

    @Test
    public void tstGetIdLoggedUser() {
        //SETUP
        UsernamePasswordAuthenticationToken dto = new UsernamePasswordAuthenticationToken(1,null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(dto);

        //ACT
        Integer idUsuarioLogado = loginService.getIdLoggedUser();

        //ASSERT
        assertEquals(1, idUsuarioLogado);

    }

    @Test
    public void testGetLoggedUser(){

        // SETUP
        UsernamePasswordAuthenticationToken dto = new UsernamePasswordAuthenticationToken(1,null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(dto);
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        when(loginService.findById(anyInt())).thenReturn(Optional.of(usuarioEntity));

        // ACT
        LoginDTO loginDTO = loginService.getLoggedUser();
        loginDTO.setEmail("luiz@gemail.com");

        assertNotNull(loginDTO);
        assertEquals(usuarioEntity.getEmail(), loginDTO.getEmail());

    }

    @Test
    public void testUpdatePassword() throws RegraDeNegocioException {

        //SETUP
        UsernamePasswordAuthenticationToken dto = new UsernamePasswordAuthenticationToken(1,null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(dto);
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        LoginDTO loginDTO = loginService.getLoggedUser();

        when(loginService.findById(anyInt())).thenReturn(Optional.of(usuarioEntity));
//        when(loginService.getLoggedUser()).thenReturn(loginDTO);
        when(usuarioRepository.findByEmail(loginDTO.getEmail())).thenReturn(usuarioEntity);
        when(passwordEncoder.encode(anyString())).thenReturn("Ahu82hajij878");

        //ACT
        loginService.updatePassword("rrrrrrrrrrrrr");

        //ASSERT
        verify(usuarioRepository, times(1)).save(any(UsuarioEntity.class));

    }

    @Test(expected = RegraDeNegocioException.class)
    public void testUpdatePasswordFail() throws RegraDeNegocioException {

        //SETUP
        UsernamePasswordAuthenticationToken dto = new UsernamePasswordAuthenticationToken(1,null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(dto);
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        when(loginService.findById(anyInt())).thenReturn(Optional.of(usuarioEntity));


        //ACT
        loginService.updatePassword("rrrrrrrrrrrrr");

        //ASSERT
        verify(usuarioRepository, times(1)).save(any(UsuarioEntity.class));

    }

    @Test
    public void testSendRecoverPasswordEmail(){

        //SETUP
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        when(usuarioRepository.findByEmail(anyString())).thenReturn(usuarioEntity);

        //ACT
        loginService.sendRecoverPasswordEmail("test@gmail.com");

    }
    @Test
    public void testFindByEmailandSenha() {

        //SETUP
        Optional<UsuarioEntity> usuarioEntity = Optional.of(getUsuarioEntity());
        when(usuarioRepository.findByEmailAndSenha(anyString(), anyString())).thenReturn(usuarioEntity);

        //ACT
        Optional<UsuarioEntity> usuarioEntity1 = loginService.findByEmailAndSenha("dawdawd@gmail.com", "wadwad12");

        assertNotNull(usuarioEntity1);
        assertEquals("luiz@gemail.com", usuarioEntity1.get().getEmail());

    }


    private static UsuarioEntity getUsuarioEntity() {
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setIdUsuario(1);
        usuarioEntity.setNome("Luiz Martins");
        usuarioEntity.setEmail("luiz@gemail.com");
        usuarioEntity.setSenha("12346789");
        usuarioEntity.setImagem(null);
        usuarioEntity.setStatus(Status.ATIVO);
        usuarioEntity.setCargos(new HashSet<>());

        return usuarioEntity;
    }

    private EtapaDTO getEtapaDTO() {
        EtapaDTO etapaDTO = new EtapaDTO();
        etapaDTO.setIdEtapa(10);
        etapaDTO.setOrdemExecucao(2);
        etapaDTO.setNome("Etapa1");

        etapaDTO.setProcessos(new HashSet<>());


        return etapaDTO;
    }

    private LoginDTO getLoginDTO() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("pasap@gmail.com");
        loginDTO.setSenha("123123");


        return loginDTO;
    }

    private EdicaoCreateDTO getEdicaoCreateDTO() {
        EdicaoCreateDTO edicaoCreateDTO = new EdicaoCreateDTO();
        edicaoCreateDTO.setNome("Edicao1");
        edicaoCreateDTO.setDataInicial(LocalDate.of(2022,8,1));
        edicaoCreateDTO.setDataFinal(LocalDate.of(2022,8,10));

        return edicaoCreateDTO;
    }
    private static EdicaoEntity getEdicaoEntity() {

        EdicaoEntity edicaoEntity = new EdicaoEntity();
        edicaoEntity.setIdEdicao(10);
        edicaoEntity.setNome("Edicao1");
        edicaoEntity.setDataInicial(LocalDate.of(2022, 10, 11));
        edicaoEntity.setDataFinal(LocalDate.of(2022, 12, 10));
        edicaoEntity.setEtapas(new HashSet<>());

        return edicaoEntity;
    }

    private static EtapaEntity getEtapaEntity() {
        EtapaEntity etapaEntity = new EtapaEntity();
        etapaEntity.setIdEtapa(2);
        etapaEntity.setEdicao(getEdicaoEntity());
        etapaEntity.setNome("Etapa1");

        Set<ProcessoEntity> processoEntities = new HashSet<>();
        processoEntities.add(getProcessoEntity());
        etapaEntity.setProcessos(processoEntities);

        return etapaEntity;
    }

    private static ProcessoEntity getProcessoEntity() {
        ProcessoEntity processoEntity = new ProcessoEntity();
        processoEntity.setIdProcesso(10);
        processoEntity.setDuracaoProcesso("1dia");
        processoEntity.setEtapa(getEtapaEntity());
        processoEntity.setOrdemExecucao(1);
        processoEntity.setDiasUteis(1);
        processoEntity.setAreasEnvolvidas(new HashSet<>());
        processoEntity.setResponsaveis(new HashSet<>());

        return processoEntity;
    }

    private static ResponsavelEntity getResponsavelEntity() {
        ResponsavelEntity responsavelEntity = new ResponsavelEntity();
        responsavelEntity.setIdResponsavel(10);
        responsavelEntity.setNome("Fulano");

        Set<ProcessoEntity> processoEntities = new HashSet<>();
        processoEntities.add(getProcessoEntity());
        responsavelEntity.setProcessos(processoEntities);

        return responsavelEntity;
    }

    private static AreaEnvolvidaEntity getAreaEnvolvida() {
        AreaEnvolvidaEntity areaEnvolvidaEntity = new AreaEnvolvidaEntity();
        areaEnvolvidaEntity.setNome("Area1");
        areaEnvolvidaEntity.setIdAreaEnvolvida(10);

        Set<ProcessoEntity> processoEntities = new HashSet<>();
        processoEntities.add(getProcessoEntity());
        areaEnvolvidaEntity.setProcessos(processoEntities);

        return areaEnvolvidaEntity;
    }

}
