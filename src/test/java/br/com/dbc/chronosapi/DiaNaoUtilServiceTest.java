package br.com.dbc.chronosapi;

import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.diaNaoUtil.DiaNaoUtilCreateDTO;
import br.com.dbc.chronosapi.dto.diaNaoUtil.DiaNaoUtilDTO;
import br.com.dbc.chronosapi.entity.classes.DiaNaoUtilEntity;
import br.com.dbc.chronosapi.entity.enums.Status;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.DiaNaoUtilRepository;
import br.com.dbc.chronosapi.service.DiaNaoUtilService;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DiaNaoUtilServiceTest {
    @InjectMocks
    private DiaNaoUtilService diaNaoUtilService;
    @Mock
    private DiaNaoUtilRepository diaNaoUtilRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(diaNaoUtilService, "objectMapper", objectMapper);
    }

    @Test
    public void testCreateDiaNaoUtilWithINATIVOSucess() throws RegraDeNegocioException {
        DiaNaoUtilCreateDTO diaNaoUtilCreateDTO = getDiaNaoUtilCreateDTO();
        DiaNaoUtilEntity diaNaoUtilEntity = getDiaNaoUtilEntity();
        diaNaoUtilEntity.setRepeticaoAnual(Status.ATIVO);
        when(diaNaoUtilRepository.save(any(DiaNaoUtilEntity.class))).thenReturn(diaNaoUtilEntity);

        DiaNaoUtilDTO diaNaoUtilDTO = diaNaoUtilService.create(diaNaoUtilCreateDTO);

        assertNotNull(diaNaoUtilDTO);
        assertEquals(10, diaNaoUtilDTO.getIdDiaNaoUtil());
    }

    @Test
    public void testCreateDiaNaoUtilWithATIVOSucess() throws RegraDeNegocioException {
        DiaNaoUtilCreateDTO diaNaoUtilCreateDTO = getDiaNaoUtilCreateDTO();
        DiaNaoUtilEntity diaNaoUtilEntity = getDiaNaoUtilEntity();
        when(diaNaoUtilRepository.save(any(DiaNaoUtilEntity.class))).thenReturn(diaNaoUtilEntity);

        DiaNaoUtilDTO diaNaoUtilDTO = diaNaoUtilService.create(diaNaoUtilCreateDTO);

        assertNotNull(diaNaoUtilDTO);
        assertEquals(10, diaNaoUtilDTO.getIdDiaNaoUtil());
    }
    @Test
    public void testUpdateDiaNaoUtilWithAtivoSucess() throws RegraDeNegocioException {

        DiaNaoUtilCreateDTO diaNaoUtilCreateDTO = getDiaNaoUtilCreateDTO();
        DiaNaoUtilEntity diaNaoUtilEntity = getDiaNaoUtilEntity();
        DiaNaoUtilEntity diaNaoUtilEntityUpdate = getDiaNaoUtilEntity();
        diaNaoUtilEntityUpdate.setDescricao("Ano novo");
        diaNaoUtilEntityUpdate.setRepeticaoAnual(Status.ATIVO);
        when(diaNaoUtilRepository.findById(anyInt())).thenReturn(Optional.of(diaNaoUtilEntity));
        when(diaNaoUtilRepository.save(any())).thenReturn(diaNaoUtilEntityUpdate);

        DiaNaoUtilDTO diaNaoUtilDTO = diaNaoUtilService.update(diaNaoUtilEntity.getIdDiaNaoUtil(), diaNaoUtilCreateDTO);

        assertNotNull(diaNaoUtilDTO);
        assertEquals("Ano novo", diaNaoUtilDTO.getDescricao());
    }

    @Test
    public void testUpdateDiaNaoUtilWithInAtivoSucess() throws RegraDeNegocioException {

        DiaNaoUtilCreateDTO diaNaoUtilCreateDTO = getDiaNaoUtilCreateDTO();
        DiaNaoUtilEntity diaNaoUtilEntity = getDiaNaoUtilEntity();
        DiaNaoUtilEntity diaNaoUtilEntityUpdate = getDiaNaoUtilEntity();
        diaNaoUtilEntityUpdate.setDescricao("Ano novo");
        diaNaoUtilEntityUpdate.setRepeticaoAnual(Status.INATIVO);
        when(diaNaoUtilRepository.findById(anyInt())).thenReturn(Optional.of(diaNaoUtilEntity));
        when(diaNaoUtilRepository.save(any())).thenReturn(diaNaoUtilEntityUpdate);

        DiaNaoUtilDTO diaNaoUtilDTO = diaNaoUtilService.update(diaNaoUtilEntity.getIdDiaNaoUtil(), diaNaoUtilCreateDTO);

        assertNotNull(diaNaoUtilDTO);
        assertEquals("Ano novo", diaNaoUtilDTO.getDescricao());
    }

    @Test
    public void testDeleteDiaNaoUtilSucess() throws RegraDeNegocioException {
        DiaNaoUtilEntity diaNaoUtilEntity = getDiaNaoUtilEntity();
        when(diaNaoUtilRepository.findById(anyInt())).thenReturn(Optional.of(diaNaoUtilEntity));

        diaNaoUtilService.delete(diaNaoUtilEntity.getIdDiaNaoUtil());

        verify(diaNaoUtilRepository, times(1)).delete(any());
    }

    @Test
    public void testFindByIdWithSuccess() throws RegraDeNegocioException {
        // SETUP
        DiaNaoUtilEntity diaNaoUtilEntity = getDiaNaoUtilEntity();
        when(diaNaoUtilRepository.findById(anyInt())).thenReturn(Optional.of(diaNaoUtilEntity));

        // ACT
        DiaNaoUtilEntity diaNaoUtil = diaNaoUtilService.findById(diaNaoUtilEntity.getIdDiaNaoUtil());

        // ASSERT
        assertNotNull(diaNaoUtil);
        assertEquals(10, diaNaoUtilEntity.getIdDiaNaoUtil());

    }

    @Test
    public void testListSucess(){
        // SETUP
        Integer pagina = 10;
        Integer quantidade = 5;

        DiaNaoUtilEntity diaNaoUtilEntity = getDiaNaoUtilEntity();
        Page<DiaNaoUtilEntity> paginaMock = new PageImpl<>(List.of(diaNaoUtilEntity));
        when(diaNaoUtilRepository.findAll(any(Pageable.class))).thenReturn(paginaMock);

        // ACT
        PageDTO<DiaNaoUtilDTO> paginaSolicitada = diaNaoUtilService.list(pagina, quantidade);

        // ASSERT
        assertNotNull(paginaSolicitada);
        assertNotNull(paginaSolicitada.getPagina());
        assertEquals(1, paginaSolicitada.getTotalElementos());
    }

    private static DiaNaoUtilCreateDTO getDiaNaoUtilCreateDTO() {
        DiaNaoUtilCreateDTO diaNaoUtilCreateDTO = new DiaNaoUtilCreateDTO();
        diaNaoUtilCreateDTO.setDescricao("natal");
        diaNaoUtilCreateDTO.setDataInicial(LocalDate.parse("1900-12-25"));
        diaNaoUtilCreateDTO.setDataFinal(LocalDate.parse("2022-12-25"));
        return diaNaoUtilCreateDTO;
    }

    private static DiaNaoUtilEntity getDiaNaoUtilEntity() {
        DiaNaoUtilEntity diaNaoUtilEntity = new DiaNaoUtilEntity();
        diaNaoUtilEntity.setRepeticaoAnual(Status.INATIVO);
        diaNaoUtilEntity.setDescricao("natal");
        diaNaoUtilEntity.setDataInicial(LocalDate.parse("1900-12-25"));
        diaNaoUtilEntity.setDataFinal(LocalDate.parse("2022-12-25"));
        diaNaoUtilEntity.setIdDiaNaoUtil(10);

        return diaNaoUtilEntity;
    }

}
