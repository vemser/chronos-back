package br.com.dbc.chronosapi;

import br.com.dbc.chronosapi.dto.usuario.CargoDTO;
import br.com.dbc.chronosapi.dto.usuario.UsuarioDTO;
import br.com.dbc.chronosapi.entity.classes.CargoEntity;
import br.com.dbc.chronosapi.entity.classes.FotoEntity;
import br.com.dbc.chronosapi.entity.classes.UsuarioEntity;
import br.com.dbc.chronosapi.entity.enums.Status;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.FotoRepository;
import br.com.dbc.chronosapi.repository.UsuarioRepository;
import br.com.dbc.chronosapi.service.FotoService;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FotoServiceTest {

    @InjectMocks
    private FotoService fotoService;

    @Mock
    private FotoRepository fotoRepository;

    @Mock
    private LoginService loginService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioService usuarioService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(usuarioService, "objectMapper", objectMapper);
    }

    @Test
    public void testFindByIdSucess() throws IOException, RegraDeNegocioException {
        FotoEntity fotoEntity = getFotoEntity();
        when(fotoRepository.findById(anyInt())).thenReturn(Optional.of(fotoEntity));

        FotoEntity fotoEntity1 = fotoService.findById(1);

        assertNotNull(fotoEntity1);
        assertEquals(1, fotoEntity1.getIdFoto());
    }

    @Test
    public void testSalvarUsuarioComFotoDTOSucess() throws IOException {
        UsuarioDTO usuarioDTO = getUsuarioDTO();
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        FotoEntity fotoEntity = getFotoEntity();
        byte[] imagemBytes = new byte[5*1024];
        MultipartFile imagem = new MockMultipartFile("imagem", imagemBytes);
        when(fotoRepository.findByUsuario(any())).thenReturn(usuarioEntity.getFoto());
        when(usuarioService.salvarUsuario(any())).thenReturn(usuarioDTO);
        when(fotoRepository.save(any())).thenReturn(fotoEntity);

        UsuarioDTO usuarioDTO1 = fotoService.salvarUsuarioComFotoDTO(imagem, usuarioEntity);

        assertNotNull(usuarioDTO1);

    }

    @Test
    public void testUploadImageSucess() throws RegraDeNegocioException, IOException {
        UsuarioDTO usuarioDTO = getUsuarioDTO();
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        byte[] imagemBytes = new byte[5*1024];
        MultipartFile imagem = new MockMultipartFile("imagem", imagemBytes);
        FotoEntity fotoEntity = getFotoEntity();
        fotoEntity.setUsuario(usuarioEntity);
        usuarioEntity.setFoto(fotoEntity);

        when(usuarioService.findById(anyInt())).thenReturn(usuarioEntity);
        when(usuarioService.findById(anyInt())).thenReturn(usuarioEntity);
        when(usuarioService.salvarUsuario(any())).thenReturn(usuarioDTO);
        when(fotoRepository.save(any())).thenReturn(fotoEntity);
        when(fotoRepository.findByUsuario(any(UsuarioEntity.class))).thenReturn(fotoEntity);

        UsuarioDTO usuarioDTO1 = fotoService.uploadImage(usuarioEntity.getIdUsuario(), imagem);

        assertNotNull(usuarioDTO1);

    }

    @Test
    public void testUploadImagePerfil() throws RegraDeNegocioException, IOException {
        UsuarioDTO usuarioDTO1 = getUsuarioDTO();
        byte[] imagemBytes = new byte[5*1024];
        MultipartFile imagem = new MockMultipartFile("imagem", imagemBytes);
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        usuarioEntity.setFoto(getFotoEntity());
        FotoEntity fotoEntity = getFotoEntity();
        fotoEntity.setUsuario(usuarioEntity);

        Set<CargoEntity> cargoEntities = new HashSet<>();
        cargoEntities.add(getCargoEntity());
        usuarioEntity.setCargos(cargoEntities);

        Set<CargoDTO> cargoDTOS = new HashSet<>();
        cargoDTOS.add(getCargoDTO());
        usuarioDTO1.setCargos(cargoDTOS);

        when(loginService.getIdLoggedUser()).thenReturn(usuarioEntity.getIdUsuario());
        when(usuarioService.findById(anyInt())).thenReturn(usuarioEntity);
        when(usuarioService.salvarUsuario(any())).thenReturn(usuarioDTO1);
        when(fotoRepository.save(any())).thenReturn(fotoEntity);
        when(fotoRepository.findByUsuario(any(UsuarioEntity.class))).thenReturn(fotoEntity);


        UsuarioDTO usuarioDTO = fotoService.uploadImagePerfil(imagem);

        assertNotNull(usuarioDTO);

    }

    @Test
    public void testSalvarUsuarioComFotoDTOElseSucess() throws IOException {
        UsuarioDTO usuarioDTO = getUsuarioDTO();

        UsuarioEntity usuarioEntity = getUsuarioEntity();
        FotoEntity fotoEntity = getFotoEntity();
        byte[] imagemBytes = new byte[5*1024];
        MultipartFile imagem = new MockMultipartFile("imagem", imagemBytes);
        fotoEntity.setUsuario(usuarioEntity);
        usuarioEntity.setFoto(fotoEntity);
        when(fotoRepository.findByUsuario(any())).thenReturn(usuarioEntity.getFoto());
        when(usuarioService.salvarUsuario(any())).thenReturn(usuarioDTO);
        when(fotoRepository.save(any())).thenReturn(fotoEntity);

        UsuarioDTO usuarioDTO1 = fotoService.salvarUsuarioComFotoDTO(imagem, usuarioEntity);

        assertNotNull(usuarioDTO1);

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

    private static UsuarioEntity getUsuarioEntity() {
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setIdUsuario(1);
        usuarioEntity.setNome("Luiz Martins");
        usuarioEntity.setEmail("luiz@gemail.com");
        usuarioEntity.setSenha("12345");
        usuarioEntity.setStatus(Status.ATIVO);
        usuarioEntity.setCargos(new HashSet<>());

        return usuarioEntity;
    }

    private static FotoEntity getFotoEntity() throws IOException {
        FotoEntity fotoEntity = new FotoEntity();
        byte[] imagemBytes = new byte[5*1024];
        MultipartFile imagem = new MockMultipartFile("imagem", imagemBytes);
        String nomeFoto = StringUtils.cleanPath((imagem.getOriginalFilename()));
        fotoEntity.setIdFoto(1);
        fotoEntity.setTipo(imagem.getContentType());
        fotoEntity.setArquivo(imagem.getBytes());
        fotoEntity.setNome(nomeFoto);
        return fotoEntity;
    }

    private static FotoEntity getFotoEntity1() throws IOException {
        FotoEntity fotoEntity = new FotoEntity();
        byte[] imagemBytes = new byte[5*1024];
        MultipartFile imagem = new MockMultipartFile("imagem", imagemBytes);
        String nomeFoto = StringUtils.cleanPath((imagem.getOriginalFilename()));
        fotoEntity.setIdFoto(2);
        fotoEntity.setTipo(imagem.getContentType());
        fotoEntity.setArquivo(imagem.getBytes());
        fotoEntity.setNome(nomeFoto);
        return fotoEntity;
    }

    private static CargoDTO getCargoDTO() {
        CargoDTO cargoDTO = new CargoDTO();
        cargoDTO.setNome("ROLE_ADMIN");
        cargoDTO.setDescricao("Administrador");
        cargoDTO.setIdCargo(12);
        return cargoDTO;
    }

    private static CargoEntity getCargoEntity() {
        CargoEntity cargoEntity = new CargoEntity();
        cargoEntity.setNome("ROLE_ADMIN");
        return cargoEntity;
    }

}
