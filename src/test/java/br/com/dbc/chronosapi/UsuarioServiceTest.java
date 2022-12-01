package br.com.dbc.chronosapi;

import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.usuario.UAdminUpdateDTO;
import br.com.dbc.chronosapi.dto.usuario.UsuarioCreateDTO;
import br.com.dbc.chronosapi.dto.usuario.UsuarioDTO;
import br.com.dbc.chronosapi.dto.usuario.UsuarioUpdateDTO;
import br.com.dbc.chronosapi.entity.classes.CargoEntity;
import br.com.dbc.chronosapi.entity.classes.UsuarioEntity;
import br.com.dbc.chronosapi.entity.enums.Status;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.UsuarioRepository;
import br.com.dbc.chronosapi.service.CargoService;
import br.com.dbc.chronosapi.service.EmailService;
import br.com.dbc.chronosapi.service.LoginService;
import br.com.dbc.chronosapi.service.UsuarioService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CargoService cargoService;

    @Mock
    private EmailService emailService;

    @Mock
    private LoginService loginService;

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(usuarioService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarListComSucesso(){
        // SETUP
        Integer pagina = 5;
        Integer quantidade = 3;

        UsuarioEntity usuarioEntity = getUsuarioEntity();
        Page<UsuarioEntity> paginaMock = new PageImpl<>(List.of(usuarioEntity));
        when(usuarioRepository.findAll(any(Pageable.class))).thenReturn(paginaMock);

        // ACT
        PageDTO<UsuarioDTO> usuarioDTO = usuarioService.list(pagina, quantidade);

        // ASSERT
        assertNotNull(usuarioDTO);
        assertEquals(1, usuarioDTO.getQuantidadePaginas());
        assertEquals(1, usuarioDTO.getTotalElementos());
    }

    @Test
    public void deveTestarUploadImageComSucesso() throws RegraDeNegocioException, IOException {
        // SETUP
        UsuarioEntity usuario = getUsuarioEntity();
        Integer idUsuario = 1;
        byte[] imagemBytes = new byte[5*1024];
        MultipartFile imagem = new MockMultipartFile("imagem", imagemBytes);

        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any())).thenReturn(getUsuarioEntity());

        // ACT
        UsuarioDTO usuarioDTO = usuarioService.uploadImage(idUsuario, imagem);

        // ASSERT
        assertNotEquals(usuario.getImagem(), usuarioDTO.getImagem());
        assertNotNull(usuarioDTO);
    }

    @Test
    public void deveTestarUploadImageComSucessoPerfil() throws RegraDeNegocioException, IOException {
        // SETUP
        UsuarioEntity usuario = getUsuarioEntity();
        byte[] imagemBytes = new byte[5*1024];
        MultipartFile imagem = new MockMultipartFile("imagem", imagemBytes);

        when(loginService.getIdLoggedUser()).thenReturn(usuario.getIdUsuario());
        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any())).thenReturn(getUsuarioEntity());

        // ACT
        UsuarioDTO usuarioDTO = usuarioService.uploadImagePerfil(imagem);

        // ASSERT
        assertNotEquals(usuario.getImagem(), usuarioDTO.getImagem());
        assertNotNull(usuarioDTO);
    }

    @Test
    public void deveTestarCreateComSucesso() throws RegraDeNegocioException, IOException {
        // SETUP
        String senhaCriptografada = "$oieufr9873he4j809fy43";
        UsuarioCreateDTO usuarioCreate = getUsuarioCreateDTO();

        when(passwordEncoder.encode(anyString())).thenReturn(senhaCriptografada);
        when(usuarioRepository.save(any())).thenReturn(getUsuarioEntity());

        // ACT
        UsuarioDTO usuarioDTO = usuarioService.create(usuarioCreate);

        // ASSERT
        assertNotNull(usuarioDTO);
        assertEquals(1, usuarioDTO.getIdUsuario());
        verify(usuarioRepository, times(1)).save(any());
        verify(emailService, times(1)).sendEmailEnvioSenha(any(), any());
    }

    @Test
    public void deveTestarUpdatePerfilComSucesso() throws RegraDeNegocioException {
        // SETUP
        UsernamePasswordAuthenticationToken dto
                = new UsernamePasswordAuthenticationToken(1, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(dto);
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        String senhaCriptografada = "$oieufr9873he4j809fy43";
        UsuarioUpdateDTO usuarioUpdateDTO = getUsuarioUpdateDTO();

        when(passwordEncoder.encode(anyString())).thenReturn(senhaCriptografada);
        when(usuarioRepository.save(any())).thenReturn(getUsuarioEntity());
        when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuarioEntity));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        // ACT
        UsuarioDTO usuarioDTO = usuarioService.updatePerfil(usuarioUpdateDTO);

        // ASSERT
        assertNotNull(usuarioDTO);
        assertEquals(1, usuarioDTO.getIdUsuario());
        assertEquals("Luiz Martins", usuarioDTO.getNome());
        verify(usuarioRepository, times(1)).save(any());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarUpdatePerfilComSenhasIncompativeis() throws RegraDeNegocioException {
        // SETUP
        UsernamePasswordAuthenticationToken dto
                = new UsernamePasswordAuthenticationToken(1, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(dto);
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        UsuarioUpdateDTO usuarioUpdateDTO = getUsuarioUpdateDTO();
        usuarioUpdateDTO.setConfirmacaoNovaSenha("123456789");

        when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuarioEntity));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        // ACT
        UsuarioDTO usuarioDTO = usuarioService.updatePerfil(usuarioUpdateDTO);

        // ASSERT
        assertNotNull(usuarioDTO);
        assertEquals(1, usuarioDTO.getIdUsuario());
        assertEquals("Luiz Martins", usuarioDTO.getNome());
        verify(usuarioRepository, times(1)).save(any());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarUpdatePerfilComSenhaAtualInvalida() throws RegraDeNegocioException {
        // SETUP
        UsernamePasswordAuthenticationToken dto
                = new UsernamePasswordAuthenticationToken(1, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(dto);
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        UsuarioUpdateDTO usuarioUpdateDTO = getUsuarioUpdateDTO();

        when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuarioEntity));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        // ACT
        UsuarioDTO usuarioDTO = usuarioService.updatePerfil(usuarioUpdateDTO);

        // ASSERT
        assertNotNull(usuarioDTO);
        assertEquals(1, usuarioDTO.getIdUsuario());
        assertEquals("Luiz Martins", usuarioDTO.getNome());
        verify(usuarioRepository, times(1)).save(any());
    }

    @Test
    public void deveTestarUpdateAdminComSucesso() throws RegraDeNegocioException {
        // SETUP
        Integer idUsuario = 1;
        UAdminUpdateDTO uAdminUpdateDTO = getUAdminUpdateDTO();
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuarioEntity));
        when(usuarioRepository.save(any())).thenReturn(getUsuarioEntity());

        // ACT
        UsuarioDTO usuarioDTO = usuarioService.updateAdmin(idUsuario, uAdminUpdateDTO);

        // ASSERT
        assertNotNull(usuarioDTO);
        assertEquals(1, usuarioDTO.getIdUsuario());
        assertEquals("Luiz Martins", usuarioDTO.getNome());
        verify(usuarioRepository, times(1)).save(any());
    }

    @Test
    public void deveTestarEnableOrDisableComSucesso() throws RegraDeNegocioException{
        // SETUP
        Integer idUsuario = 1;
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuarioEntity));
        when(usuarioRepository.save(any())).thenReturn(usuarioEntity);

        // ACT
        UsuarioDTO usuarioDTO = usuarioService.enableOrDisable(idUsuario);

        // ASSERT
        assertNotNull(usuarioDTO);
        assertEquals(Status.INATIVO, usuarioDTO.getStatus());
        verify(usuarioRepository, times(1)).save(any());
    }

    @Test
    public void deveTestarEnableOrDisableComErro() throws RegraDeNegocioException {
        // SETUP
        Integer idUsuario = 1;
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        usuarioEntity.setStatus(Status.INATIVO);

        when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuarioEntity));
        when(usuarioRepository.save(any())).thenReturn(usuarioEntity);

        // ACT
        UsuarioDTO usuarioDTO = usuarioService.enableOrDisable(idUsuario);

        // ASSERT
        assertNotNull(usuarioDTO);
        assertEquals(Status.ATIVO, usuarioDTO.getStatus());
        verify(usuarioRepository, times(1)).save(any());
    }

    @Test
    public void deveTestarDeleteComSucesso() throws RegraDeNegocioException {
        // SETUP
        Integer idUsuario = 1;
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        usuarioEntity.setIdUsuario(1);
        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.of(usuarioEntity));

        // Ação (ACT)
        usuarioService.delete(idUsuario);

        // Verificação (ASSERT)
        verify(usuarioRepository, times(1)).delete(any());
    }

    @Test
    public void deveTestarFindByEmailComSucesso() throws RegraDeNegocioException {
        // Criar variaveis (SETUP)
        String email = "luiz@dbccompany.com.br";
        UsuarioEntity usuarioRecuperado = getUsuarioEntity();
        usuarioRecuperado.setEmail(email);
        when(usuarioRepository.findByEmail(anyString())).thenReturn(usuarioRecuperado);

        // Ação (ACT)
        UsuarioEntity usuarioEntity = usuarioService.findByEmail(email);

        // Verificação (ASSERT)
        assertNotNull(usuarioEntity);
        assertEquals(email, usuarioEntity.getEmail());
    }

    private static UsuarioEntity getUsuarioEntity() {
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setIdUsuario(1);
        usuarioEntity.setNome("Luiz Martins");
        usuarioEntity.setEmail("luiz@gemail.com");
        usuarioEntity.setSenha("12345");
        usuarioEntity.setImagem(null);
        usuarioEntity.setStatus(Status.ATIVO);
        usuarioEntity.setCargos(new HashSet<>());

        return usuarioEntity;
    }

    private static UsuarioCreateDTO getUsuarioCreateDTO() {
        UsuarioCreateDTO usuarioCreateDTO = new UsuarioCreateDTO();
        usuarioCreateDTO.setNome("Luiz ");
        usuarioCreateDTO.setCargos(new HashSet<>());

        return usuarioCreateDTO;
    }

    private static UsuarioUpdateDTO getUsuarioUpdateDTO() {
        UsuarioUpdateDTO usuarioUpdateDTO = new UsuarioUpdateDTO();
        usuarioUpdateDTO.setNome("Luiz Martins");
        usuarioUpdateDTO.setSenhaAtual("12345");
        usuarioUpdateDTO.setNovaSenha("123");
        usuarioUpdateDTO.setConfirmacaoNovaSenha("123");

        return usuarioUpdateDTO;
    }

    private static UAdminUpdateDTO getUAdminUpdateDTO() {
        UAdminUpdateDTO uAdminUpdateDTO = new UAdminUpdateDTO();
        uAdminUpdateDTO.setNome("Luiz Martins");
        uAdminUpdateDTO.setCargos(new ArrayList<>());

        return uAdminUpdateDTO;
    }

    private static CargoEntity getCargoEntity() {
        CargoEntity cargoEntity = new CargoEntity();
        cargoEntity.setNome("ROLE_ADMIN");
        return cargoEntity;
    }
}
