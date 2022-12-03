package br.com.dbc.chronosapi;

import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.edicao.EdicaoCreateDTO;
import br.com.dbc.chronosapi.dto.etapa.EtapaCreateDTO;
import br.com.dbc.chronosapi.dto.etapa.EtapaDTO;
import br.com.dbc.chronosapi.dto.processo.ProcessoDTO;
import br.com.dbc.chronosapi.entity.classes.EdicaoEntity;
import br.com.dbc.chronosapi.entity.classes.EtapaEntity;
import br.com.dbc.chronosapi.entity.classes.processos.AreaEnvolvidaEntity;
import br.com.dbc.chronosapi.entity.classes.processos.ProcessoEntity;
import br.com.dbc.chronosapi.entity.classes.processos.ResponsavelEntity;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.EtapaRepository;
import br.com.dbc.chronosapi.repository.ProcessoRepository;
import br.com.dbc.chronosapi.service.EdicaoService;
import br.com.dbc.chronosapi.service.EtapaService;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EtapaServiceTest {

    @InjectMocks
    private EtapaService etapaService;
    @Mock
    private EtapaRepository etapaRepository;
    @Mock
    private EdicaoService edicaoService;

    @Mock
    private ProcessoRepository processoRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(etapaService, "objectMapper", objectMapper);
    }

    @Test
    public void testCreateEtapaSuccess() throws RegraDeNegocioException {
        //SETUP
        EtapaCreateDTO etapaCreateDTO = getEtapaCreateDTO();
        EtapaEntity etapaEntity = getEtapaEntity();

        when(etapaRepository.save(any(EtapaEntity.class))).thenReturn(etapaEntity);
        when(edicaoService.findById(anyInt())).thenReturn(getEdicaoEntity());

        //ACT
        EtapaDTO etapaDTO = etapaService.create(1, etapaCreateDTO);

        //ASSERT
        assertNotNull(etapaDTO);
        assertEquals(10, etapaDTO.getIdEtapa());
    }

    @Test
    public void testListEtapasDaEdicaoSucess() throws RegraDeNegocioException {
        EdicaoEntity edicaoEntity = getEdicaoEntity();
        EtapaEntity etapaEntity = getEtapaEntity();

        Set<ProcessoEntity> processoEntitySet = new HashSet<>();
        processoEntitySet.add(getProcessoEntity());
        processoEntitySet.add(getProcessoEntity2());

        etapaEntity.setProcessos(processoEntitySet);
        Set<EtapaEntity> setEtapas = new HashSet<>();
        setEtapas.add(etapaEntity);


        ProcessoEntity processoEntity = getProcessoEntity();
        List<ProcessoEntity> listProcessos = new ArrayList<>();
        listProcessos.add(processoEntity);
        edicaoEntity.setEtapas(setEtapas);
        when(edicaoService.findById(any())).thenReturn(edicaoEntity);


        List<EtapaDTO> etapaDTO = etapaService.listEtapasDaEdicao(edicaoEntity.getIdEdicao());

        assertNotNull(etapaDTO);
        assertTrue(etapaDTO.size()> 0);
        assertEquals(1, etapaDTO.size());
    }

    @Test
    public void testEtapaUpdateSuccess() throws RegraDeNegocioException {

        // SETUP
        EtapaCreateDTO etapaCreateDTO = getEtapaCreateDTO();

        EtapaEntity etapaEntity = getEtapaEntity();
        EtapaEntity etapaEntity1 = getEtapaEntity();
        etapaEntity1.setNome("nomeDiferente");

        when(etapaRepository.findById(anyInt())).thenReturn(Optional.of(etapaEntity));
        when(etapaRepository.save(any())).thenReturn(etapaEntity1);

        // ACT
        EtapaDTO etapaDTO = etapaService.update(etapaEntity.getIdEtapa(), etapaCreateDTO);

        // ASSERT
        assertNotNull(etapaDTO);
        assertEquals("nomeDiferente", etapaDTO.getNome());
    }

    @Test
    public void testEtapaDeleteSucess() throws RegraDeNegocioException {
        // SETUP

        EtapaEntity etapaEntity = getEtapaEntity();

        when(etapaRepository.findById(anyInt())).thenReturn(Optional.of(etapaEntity));


        // ACT

        etapaService.delete(etapaEntity.getIdEtapa());

        // ASSERT
        verify(etapaRepository, times(1)).delete(any());

    }

    @Test
    public void testFindByIdWithSuccess() throws RegraDeNegocioException {
        // SETUP
        EtapaEntity etapaEntity = getEtapaEntity();
        when(etapaRepository.findById(anyInt())).thenReturn(Optional.of(etapaEntity));

        // ACT
        EtapaEntity etapa = etapaService.findById(etapaEntity.getIdEtapa());

        // ASSERT
        assertNotNull(etapa);
        assertEquals(10, etapaEntity.getIdEtapa());

    }

    @Test(expected = RegraDeNegocioException.class)
    public void testFindByIdWithFail() throws RegraDeNegocioException {
        // Criar variaveis (SETUP)
        Integer busca = 10;
        when(etapaRepository.findById(anyInt())).thenReturn(Optional.empty());


        // Ação (ACT)
        EtapaEntity etapaEntity = etapaService.findById(busca);

        //Assert
        assertNull(etapaEntity);
    }

    @Test
    public void testListSucess(){
        // SETUP
        Integer pagina = 10;
        Integer quantidade = 5;

        EtapaEntity etapaEntity = getEtapaEntity();
        Set<ProcessoEntity> processoEntities = new HashSet<>();
        processoEntities.add(getProcessoEntity2());
        etapaEntity.setProcessos(processoEntities);
        Page<EtapaEntity> paginaMock = new PageImpl<>(List.of(etapaEntity));
        when(etapaRepository.findAll(any(Pageable.class))).thenReturn(paginaMock);

        List<ProcessoEntity> listProcessos = new ArrayList<>();
        listProcessos.add(getProcessoEntity());

        // ACT
        PageDTO<EtapaDTO> paginaSolicitada = etapaService.list(pagina, quantidade);

        // ASSERT
        assertNotNull(paginaSolicitada);
        assertNotNull(paginaSolicitada.getPagina());
        assertEquals(1, paginaSolicitada.getTotalElementos());
    }

    @Test
    public void testSaveSuccess(){

        //SETUP
        EtapaEntity etapaEntity = getEtapaEntity();

        //ACT
        EtapaDTO etapaDTO = etapaService.save(etapaEntity);

        //ASSERT
        assertEquals(10, etapaDTO.getIdEtapa());

    }

    @Test
    public void testProcessosDTO(){
        Set<ProcessoEntity> processoEntitySet = new HashSet<>();
        processoEntitySet.add(getProcessoEntity());

        List<ProcessoDTO> processoDTOS = etapaService.getProcessosDTO(processoEntitySet);

        assertNotNull(processoDTOS);
    }

    private EdicaoCreateDTO getEdicaoCreateDTO() {
        EdicaoCreateDTO edicaoCreateDTO = new EdicaoCreateDTO();
        edicaoCreateDTO.setNome("Edicao1");
        edicaoCreateDTO.setDataInicial(LocalDate.of(2022,8,1));
        edicaoCreateDTO.setDataFinal(LocalDate.of(2022,8,10));
        return edicaoCreateDTO;
    }

    private EtapaCreateDTO getEtapaCreateDTO() {
        EtapaCreateDTO etapaCreateDTO = new EtapaCreateDTO();
        etapaCreateDTO.setNome("Etapa1");

        return etapaCreateDTO;
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

    private static ProcessoEntity getProcessoEntity2() {
        ProcessoEntity processoEntity = new ProcessoEntity();
        processoEntity.setIdProcesso(9);
        processoEntity.setDuracaoProcesso("2dia");
        processoEntity.setOrdemExecucao(1);
        processoEntity.setDiasUteis(1);
        processoEntity.setAreasEnvolvidas(new HashSet<>());
        processoEntity.setResponsaveis(new HashSet<>());

        return processoEntity;
    }

    private static EtapaEntity getEtapaEntity() {
        EtapaEntity etapaEntity = new EtapaEntity();
        etapaEntity.setIdEtapa(10);
        etapaEntity.setEdicao(getEdicaoEntity());
        etapaEntity.setNome("Etapa1");

        etapaEntity.setProcessos(new HashSet<>());

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

    private static ProcessoDTO getProcessoDTO() {
        ProcessoDTO processoDTO = new ProcessoDTO();
        processoDTO.setIdProcesso(10);
        processoDTO.setDuracaoProcesso("1dia");
        processoDTO.setOrdemExecucao(1);
        processoDTO.setDiasUteis(1);
        processoDTO.setAreasEnvolvidas(new HashSet<>());
        processoDTO.setResponsaveis(new HashSet<>());

        return processoDTO;
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

