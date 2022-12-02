package br.com.dbc.chronosapi;

import br.com.dbc.chronosapi.dto.processo.AreaEnvolvidaCreateDTO;
import br.com.dbc.chronosapi.dto.processo.AreaEnvolvidaDTO;
import br.com.dbc.chronosapi.entity.classes.processos.AreaEnvolvidaEntity;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

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
    public  void testCreateAreaEnvolvidaSucess() {
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

    @Test
    public void testAreaEnvolvidaDeleteSuccess() throws RegraDeNegocioException {
        // SETUP
        AreaEnvolvidaEntity areaEnvolvida = getAreaEnvolvidaEntity();

        when(areaEnvolvidaRepository.findById(anyInt())).thenReturn(Optional.of(areaEnvolvida));

        // ACT
        areaEnvolvidaService.delete(areaEnvolvida.getIdAreaEnvolvida());

        // ASSERT
        verify(areaEnvolvidaRepository, times(1)).delete(any());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void testAreaEnvolvidaDeleteFail() throws RegraDeNegocioException {
        // SETUP
        when(areaEnvolvidaRepository.findById(anyInt())).thenReturn(Optional.empty());

        // ACT
        areaEnvolvidaService.delete(5);

        // ASSERT
        verify(areaEnvolvidaRepository, times(1)).delete(any());
    }

    @Test
    public void testFindByIdSuccess() throws RegraDeNegocioException {
        // SETUP
        AreaEnvolvidaEntity areaEnvolvidaEntity = getAreaEnvolvidaEntity();
        when(areaEnvolvidaRepository.findById(anyInt())).thenReturn(Optional.of(areaEnvolvidaEntity));

        // ACT
        AreaEnvolvidaEntity areaEnvolvidaEntity1 = areaEnvolvidaService.findById(areaEnvolvidaEntity.getIdAreaEnvolvida());

        // ASSERT
        assertNotNull(areaEnvolvidaEntity1);
        assertEquals(10, areaEnvolvidaEntity1.getIdAreaEnvolvida());

    }

    @Test
    public void testFindByNome() throws RegraDeNegocioException {
        // SETUP
        AreaEnvolvidaEntity areaEnvolvidaEntity = getAreaEnvolvidaEntity();
        when(areaEnvolvidaRepository.findByNomeContainingIgnoreCase(anyString())).thenReturn(areaEnvolvidaEntity);

        // ACT
        AreaEnvolvidaEntity areaEnvolvidaEntity1 = areaEnvolvidaService.findByNomeContainingIgnoreCase(areaEnvolvidaEntity.getNome());

        // ASSERT
        assertNotNull(areaEnvolvidaEntity1);
        assertEquals("area1", areaEnvolvidaEntity1.getNome());

    }

    @Test
    public void testList(){
        // (SETUP)
        List<AreaEnvolvidaEntity> lista = new ArrayList<>();
        lista.add(getAreaEnvolvidaEntity());
        when(areaEnvolvidaRepository.findAll()).thenReturn(lista);

        //(ACT)
        List<AreaEnvolvidaDTO> listaDto = areaEnvolvidaService.listarAreas();

        // (ASSERT)
        assertNotNull(listaDto);
        assertTrue(listaDto.size() > 0);
        assertEquals(1, lista.size());
    }

    private static AreaEnvolvidaEntity getAreaEnvolvidaEntity() {
        AreaEnvolvidaEntity areaEnvolvidaEntity = new AreaEnvolvidaEntity();
        areaEnvolvidaEntity.setIdAreaEnvolvida(10);
        areaEnvolvidaEntity.setNome("area1");
        areaEnvolvidaEntity.setIdAreaEnvolvida(10);
        return areaEnvolvidaEntity;
    }

    private static AreaEnvolvidaCreateDTO getAreaEnvolvidaCreateDTO() {
        AreaEnvolvidaCreateDTO areaEnvolvidaCreateDTO = new AreaEnvolvidaCreateDTO();
        areaEnvolvidaCreateDTO.setNome("area1");
        return areaEnvolvidaCreateDTO;
    }

    private static AreaEnvolvidaDTO getAreaEnvolvidaDTO() {
        AreaEnvolvidaDTO areaEnvolvidaDTO = new AreaEnvolvidaDTO();
        areaEnvolvidaDTO.setIdAreaEnvolvida(10);
        areaEnvolvidaDTO.setNome("area1");
        areaEnvolvidaDTO.setIdAreaEnvolvida(10);
        return areaEnvolvidaDTO;
    }
}
