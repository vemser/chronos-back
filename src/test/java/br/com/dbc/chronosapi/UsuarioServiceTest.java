//package br.com.dbc.chronosapi;
//
//import br.com.dbc.chronosapi.dto.PageDTO;
//import br.com.dbc.chronosapi.dto.usuario.UsuarioDTO;
//import br.com.dbc.chronosapi.entity.classes.CargoEntity;
//import br.com.dbc.chronosapi.entity.classes.UsuarioEntity;
//import br.com.dbc.chronosapi.entity.enums.Status;
//import br.com.dbc.chronosapi.repository.UsuarioRepository;
//import br.com.dbc.chronosapi.service.UsuarioService;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.HashSet;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class UsuarioServiceTest {
//
//    @InjectMocks
//    private UsuarioService usuarioService;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Mock
//    private UsuarioRepository usuarioRepository;
//
//    @Before
//    public void init() {
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        ReflectionTestUtils.setField(usuarioService, "objectMapper", objectMapper);
//    }
//
//    @Test
//    public void deveTestarListComSucesso(){
//        // SETUP
//        Integer pagina = 5;
//        Integer quantidade = 3;
//
//        UsuarioEntity usuarioEntity = getUsuarioEntity();
//        Page<UsuarioEntity> paginaMock = new PageImpl<>(List.of(usuarioEntity));
//        when(usuarioRepository.findAll(any(Pageable.class))).thenReturn(paginaMock);
//
//        // ACT
//        PageDTO<UsuarioDTO> itemEntretenimentoDtoPageDTO = usuarioService.list(pagina, quantidade);
//
//        // ASSERT
//        assertNotNull(itemEntretenimentoDtoPageDTO);
//        assertEquals(1, itemEntretenimentoDtoPageDTO.getQuantidadePaginas());
//        assertEquals(1, itemEntretenimentoDtoPageDTO.getTotalElementos());
//    }
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
//    private static CargoEntity getCargoEntity() {
//        CargoEntity cargoEntity = new CargoEntity();
//        cargoEntity.setNome("ROLE_ADMIN");
//        return cargoEntity;
//    }
//}
