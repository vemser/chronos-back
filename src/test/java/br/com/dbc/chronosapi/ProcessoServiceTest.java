package br.com.dbc.chronosapi;

import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.processo.ProcessoCreateDTO;
import br.com.dbc.chronosapi.dto.processo.ProcessoDTO;
import br.com.dbc.chronosapi.entity.classes.EdicaoEntity;
import br.com.dbc.chronosapi.entity.classes.EtapaEntity;
import br.com.dbc.chronosapi.entity.classes.processos.AreaEnvolvidaEntity;
import br.com.dbc.chronosapi.entity.classes.processos.ProcessoEntity;
import br.com.dbc.chronosapi.entity.classes.processos.ResponsavelEntity;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.ProcessoRepository;
import br.com.dbc.chronosapi.service.AreaEnvolvidaService;
import br.com.dbc.chronosapi.service.EtapaService;
import br.com.dbc.chronosapi.service.ProcessoService;
import br.com.dbc.chronosapi.service.ResponsavelService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProcessoServiceTest {

    @InjectMocks
    private ProcessoService processoService;

    @Mock
    private ResponsavelService responsavelService;

    @Mock
    private AreaEnvolvidaService areaEnvolvidaService;

    @Mock
    private ProcessoRepository processoRepository;

    @Mock
    private EtapaService etapaService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(processoService, "objectMapper", objectMapper);
    }

    @Test
    public void testListSucess(){
        // SETUP
        Integer pagina = 10;
        Integer quantidade = 5;

        ProcessoEntity processoEntity = getProcessoEntity();
        Page<ProcessoEntity> paginaMock = new PageImpl<>(List.of(processoEntity));
        when(processoRepository.findAll(any(Pageable.class))).thenReturn(paginaMock);

        // ACT
        PageDTO<ProcessoDTO> paginaSolicitada = processoService.list(pagina, quantidade);

        // ASSERT
        assertNotNull(paginaSolicitada);
        assertNotNull(paginaSolicitada.getPagina());
        assertEquals(1, paginaSolicitada.getTotalElementos());
    }
    @Test
    public void testProcessoCreateSuccess() throws RegraDeNegocioException {
        //SETUP
        ProcessoCreateDTO processoCreateDTO = getProcessoCreateDTO();
        ProcessoEntity processoEntity = getProcessoEntity();
        EtapaEntity etapaEntity = getEtapaEntity();

        when(processoRepository.save(any(ProcessoEntity.class))).thenReturn(processoEntity);
        when(etapaService.findById(anyInt())).thenReturn(getEtapaEntity());

        //ACT
        ProcessoDTO processoDTO = processoService.create(10, processoCreateDTO);
        processoDTO.setIdProcesso(10);

        //ASSERT
        assertNotNull(processoDTO);
        assertEquals(10, processoDTO.getIdProcesso());
    }

    @Test
    public void testProcessoUpdateSucess() throws RegraDeNegocioException {
        // SETUP
        ProcessoCreateDTO processoCreateDTO = getProcessoCreateDTO();

        ProcessoEntity processoEntity = getProcessoEntity();
        ProcessoEntity processoEntity1 = getProcessoEntity();
        processoEntity1.setOrdemExecucao(3);
        Set<ResponsavelEntity> responsavelEntities = new HashSet<>();
        responsavelEntities.add(getResponsavelEntity());
        Set<AreaEnvolvidaEntity> areaEnvolvidaEntities = new HashSet<>();
        areaEnvolvidaEntities.add(getAreaEnvolvida());

        when(processoRepository.findById(anyInt())).thenReturn(Optional.of(processoEntity));
        when(processoRepository.save(any())).thenReturn(processoEntity1);

        // ACT

        ProcessoDTO processoDTO = processoService.update(processoEntity.getIdProcesso(), processoCreateDTO);

        // ASSERT
        assertNotNull(processoDTO);
        assertEquals(3, processoDTO.getOrdemExecucao());
    }

    @Test
    public void testEdicaoDeleteSuccess() throws RegraDeNegocioException {
        // SETUP
        ProcessoEntity processoEntity = getProcessoEntity();

        when(processoRepository.findById(anyInt())).thenReturn(Optional.of(processoEntity));

        // ACT
        processoService.delete(processoEntity.getIdProcesso());

        // ASSERT
        verify(processoRepository, times(1)).delete(any());
    }
    @Test
    public void testFindByIdSuccess() throws RegraDeNegocioException {
        // SETUP
        ProcessoEntity processoEntity = getProcessoEntity();
        when(processoRepository.findById(anyInt())).thenReturn(Optional.of(processoEntity));

        // ACT
        ProcessoEntity edicao = processoService.findById(processoEntity.getIdProcesso());

        // ASSERT
        assertNotNull(edicao);
        assertEquals(10, processoEntity.getIdProcesso());

    }

    private static EdicaoEntity getEdicaoEntity() {

        EdicaoEntity edicaoEntity = new EdicaoEntity();
        edicaoEntity.setIdEdicao(5);
        edicaoEntity.setNome("Edicao1");
        edicaoEntity.setDataInicial(LocalDate.of(2022, 10, 11));
        edicaoEntity.setDataFinal(LocalDate.of(2022, 12, 10));
        edicaoEntity.setEtapas(new HashSet<>());

        return edicaoEntity;
    }

    private static EtapaEntity getEtapaEntity() {
        EtapaEntity etapaEntity = new EtapaEntity();
        etapaEntity.setIdEtapa(10);
        etapaEntity.setEdicao(getEdicaoEntity());
        etapaEntity.setNome("Etapa1");

        etapaEntity.setProcessos(new HashSet<>());

        return etapaEntity;
    }

    private ProcessoCreateDTO getProcessoCreateDTO() {
        ProcessoCreateDTO processoCreateDTO = new ProcessoCreateDTO();
        processoCreateDTO.setNome("processo1");
        processoCreateDTO.setResponsavel(new HashSet<>());
        processoCreateDTO.setAreaEnvolvida(new HashSet<>());
        processoCreateDTO.setOrdemExecucao(10);
        processoCreateDTO.setDuracaoProcesso("1 dia");
        processoCreateDTO.setDiasUteis(2);

        return processoCreateDTO;
    }
    private static ProcessoEntity getProcessoEntity() {
        ProcessoEntity processoEntity = new ProcessoEntity();
        processoEntity.setIdProcesso(10);
        processoEntity.setDuracaoProcesso("1 dia");
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
//        responsavelEntity.setResponsavel("Fulano");
        responsavelEntity.setProcessos(new HashSet<>());

        return responsavelEntity;
    }

    private static AreaEnvolvidaEntity getAreaEnvolvida() {
        AreaEnvolvidaEntity areaEnvolvidaEntity = new AreaEnvolvidaEntity();
//        areaEnvolvidaEntity.setAreaEnvolvida("Area1");
        areaEnvolvidaEntity.setIdAreaEnvolvida(10);

        areaEnvolvidaEntity.setProcessos(new HashSet<>());

        return areaEnvolvidaEntity;
    }

}
