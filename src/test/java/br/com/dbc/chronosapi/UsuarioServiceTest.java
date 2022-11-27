package br.com.dbc.chronosapi;

import br.com.dbc.chronosapi.entity.classes.CargoEntity;
import br.com.dbc.chronosapi.entity.classes.UsuarioEntity;
import br.com.dbc.chronosapi.entity.enums.StatusUsuario;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.CargoRepository;
import br.com.dbc.chronosapi.repository.UsuarioRepository;
import br.com.dbc.chronosapi.security.TokenService;
import br.com.dbc.chronosapi.service.EmailService;
import br.com.dbc.chronosapi.service.UsuarioService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.swing.text.html.Option;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    private ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private EmailService emailService;
    @Mock
    private TokenService tokenService;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private CargoRepository cargoRepository;

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(usuarioService, "objectMapper", objectMapper);
    }

    private static UsuarioEntity getUsuarioEntity() {
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setNome("Fulano");
        usuarioEntity.setEmail("fulano@gmail.com");
        usuarioEntity.setSenha("123");
        usuarioEntity.setIdUsuario(10);
        usuarioEntity.setStatus(StatusUsuario.ATIVO);

        Set<CargoEntity> cargoEntities = new HashSet<>();
        cargoEntities.add(getCargoEntity());
        usuarioEntity.setCargos(cargoEntities);

        return usuarioEntity;
    }

    private static CargoEntity getCargoEntity() {
        CargoEntity cargoEntity = new CargoEntity();
        cargoEntity.setIdCargo(1);
        cargoEntity.setNome("ROLE_ADMIN");


        return cargoEntity;
    }

}
