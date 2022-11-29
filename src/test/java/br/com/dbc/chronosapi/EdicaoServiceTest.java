package br.com.dbc.chronosapi;

import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.edicao.EdicaoCreateDTO;
import br.com.dbc.chronosapi.dto.edicao.EdicaoDTO;
import br.com.dbc.chronosapi.entity.classes.EdicaoEntity;
import br.com.dbc.chronosapi.entity.classes.EtapaEntity;
import br.com.dbc.chronosapi.entity.classes.processos.AreaEnvolvidaEntity;
import br.com.dbc.chronosapi.entity.classes.processos.ProcessoEntity;
import br.com.dbc.chronosapi.entity.classes.processos.ResponsavelEntity;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.EdicaoRepository;
import br.com.dbc.chronosapi.service.EdicaoService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EdicaoServiceTest {

    @InjectMocks
    private EdicaoService edicaoService;
    @Mock
    private EdicaoRepository edicaoRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(edicaoService, "objectMapper", objectMapper);
    }

    @Test
    public void testCreateEdicaoSuccess() {
        //SETUP
        EdicaoCreateDTO edicaoCreateDTO = getEdicaoCreateDTO();
        EdicaoEntity edicaoEntity = getEdicaoEntity();

        when(edicaoRepository.save(any(EdicaoEntity.class))).thenReturn(edicaoEntity);

        //ACT
        EdicaoDTO edicaoDTO = edicaoService.create(edicaoCreateDTO);

        //ASSERT
        assertNotNull(edicaoDTO);
        assertEquals(10, edicaoDTO.getIdEdicao());
    }

    @Test
    public void testEdicaoUpdateSuccess() throws RegraDeNegocioException {

        // SETUP
        EdicaoCreateDTO edicaoCreateDTO = getEdicaoCreateDTO();

        EdicaoEntity edicaoEntity = getEdicaoEntity();
        EdicaoEntity edicaoEntity1 = getEdicaoEntity();
        edicaoEntity1.setNome("nomeDiferente");

        when(edicaoRepository.findById(anyInt())).thenReturn(Optional.of(edicaoEntity));
        when(edicaoRepository.save(any())).thenReturn(edicaoEntity1);

        // ACT
        EdicaoDTO edicaoDTO = edicaoService.update(edicaoEntity.getIdEdicao(), edicaoCreateDTO);

        // ASSERT
        assertNotNull(edicaoDTO);
        assertNotEquals("nomeDiferente", edicaoDTO.getNome());
    }

    @Test
    public void testEdicaoDeleteSuccess() throws RegraDeNegocioException {
        // SETUP
        EdicaoEntity edicaoEntity = getEdicaoEntity();

        when(edicaoRepository.findById(anyInt())).thenReturn(Optional.of(edicaoEntity));

        // ACT
        edicaoService.delete(edicaoEntity.getIdEdicao());

        // ASSERT
        verify(edicaoRepository, times(1)).delete(any());
    }

    @Test
    public void testFindByIdSuccess() throws RegraDeNegocioException {
        // SETUP
        EdicaoEntity edicaoEntity = getEdicaoEntity();
        when(edicaoRepository.findById(anyInt())).thenReturn(Optional.of(edicaoEntity));

        // ACT
        EdicaoEntity edicao = edicaoService.findById(edicaoEntity.getIdEdicao());

        // ASSERT
        assertNotNull(edicao);
        assertEquals(10, edicaoEntity.getIdEdicao());

    }

    @Test(expected = RegraDeNegocioException.class)
    public void testEdicaoDeleteFail() throws RegraDeNegocioException {
        // SETUP
        when(edicaoRepository.findById(anyInt())).thenReturn(Optional.empty());

        // ACT
        edicaoService.delete(5);

        // ASSERT
        verify(edicaoRepository, times(1)).delete(any());
    }
    @Test
    public void testEnableOrDisable(){

        //SET
        Integer idEdicao = 10;
        EdicaoEntity edicaoEntity = getEdicaoEntity();

        // ACT
        edicaoService.save(edicaoEntity);

        //ASSERT
        verify(edicaoRepository, times(1)).save(any());
    }

    @Test
    public void testListSucess(){
        // SETUP
        Integer pagina = 10;
        Integer quantidade = 5;

        EdicaoEntity edicaoEntity = getEdicaoEntity();
        Page<EdicaoEntity> paginaMock = new PageImpl<>(List.of(edicaoEntity));
        when(edicaoRepository.findAll(any(Pageable.class))).thenReturn(paginaMock);

        // ACT
        PageDTO<EdicaoDTO> paginaSolicitada = edicaoService.list(pagina, quantidade);

        // ASSERT
        assertNotNull(paginaSolicitada);
        assertNotNull(paginaSolicitada.getPagina());
        assertEquals(1, paginaSolicitada.getTotalElementos());
    }

    @Test
    public void testFindByIdWithSuccess() throws RegraDeNegocioException {
        // SETUP
        EdicaoEntity edicaoEntity = getEdicaoEntity();
        when(edicaoRepository.findById(anyInt())).thenReturn(Optional.of(edicaoEntity));

        // ACT
        EdicaoEntity edicao = edicaoService.findById(edicaoEntity.getIdEdicao());

        // ASSERT
        assertNotNull(edicao);
        assertEquals(10, edicaoEntity.getIdEdicao());

    }

    @Test(expected = RegraDeNegocioException.class)
    public void testFindByIdFail() throws RegraDeNegocioException {
        // SETUP
        when(edicaoRepository.findById(anyInt())).thenReturn(Optional.empty());

        // ACT
        edicaoService.findById(5);

        // ASSERT
        verify(edicaoRepository, times(1)).findById(any());

    }

    @Test
    public void testSaveSuccess(){

        //SETUP
        EdicaoEntity edicaoEntity = getEdicaoEntity();

        //ACT
        EdicaoDTO edicaoDTO = edicaoService.save(edicaoEntity);

        //ASSERT
        assertEquals(10, edicaoDTO.getIdEdicao());

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
