package br.com.dbc.chronosapi;

import br.com.dbc.chronosapi.dto.processo.AreaEnvolvidaCreateDTO;
import br.com.dbc.chronosapi.dto.processo.AreaEnvolvidaDTO;
import br.com.dbc.chronosapi.entity.classes.processos.AreaEnvolvidaEntity;
import br.com.dbc.chronosapi.repository.AreaEnvolvidaRepository;
import br.com.dbc.chronosapi.service.AreaEnvolvidaService;
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
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AreaEnvolvidaServiceTest {
    @InjectMocks
    private AreaEnvolvidaService areaEnvolvidaService;

    @Mock
    private AreaEnvolvidaRepository areaEnvolvidaRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(areaEnvolvidaService, "objectMapper", objectMapper);
    }

    @Test
    private  void testCreateAreaEnvolvidaSucess() {
        // SETUP
        AreaEnvolvidaCreateDTO areaEnvolvidaCreateDTO = getAreaEnvolvidaCreateDTO();
        AreaEnvolvidaEntity areaEnvolvidaEntity = getAreaEnvolvidaEntity();
        when(areaEnvolvidaRepository.save(any(AreaEnvolvidaEntity.class))).thenReturn(areaEnvolvidaEntity);
        // ACT
        AreaEnvolvidaDTO areaEnvolvidaDTO = areaEnvolvidaService.create(areaEnvolvidaCreateDTO);
        // ASSERT
        assertNotNull(areaEnvolvidaDTO);
        assertEquals(10, areaEnvolvidaDTO.getIdAreaEnvolvida());
    }

    private static AreaEnvolvidaEntity getAreaEnvolvidaEntity() {
        AreaEnvolvidaEntity areaEnvolvidaEntity = new AreaEnvolvidaEntity();
        areaEnvolvidaEntity.setIdAreaEnvolvida(10);
        areaEnvolvidaEntity.setNome("Area1");
        areaEnvolvidaEntity.setIdAreaEnvolvida(10);
        return areaEnvolvidaEntity;
    }

    private static AreaEnvolvidaCreateDTO getAreaEnvolvidaCreateDTO() {
        AreaEnvolvidaCreateDTO areaEnvolvidaCreateDTO = new AreaEnvolvidaCreateDTO();
        areaEnvolvidaCreateDTO.setNome("Area1");
        return areaEnvolvidaCreateDTO;
    }
}
