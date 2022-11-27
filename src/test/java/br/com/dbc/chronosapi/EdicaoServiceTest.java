package br.com.dbc.chronosapi;

import br.com.dbc.chronosapi.dto.EdicaoCreateDTO;
import br.com.dbc.chronosapi.dto.EdicaoDTO;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

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
    public void testCreateEdicaoSuccess() throws RegraDeNegocioException {
        //SETUP
        EdicaoCreateDTO edicaoCreateDTO = getEdicaoCreateDTO();
        EdicaoEntity edicaoEntity = getEdicaoEntity();

        edicaoEntity.setIdEdicao(8);

        when(edicaoService.findById(anyInt())).thenReturn(edicaoEntity);
        when(edicaoRepository.save(any(EdicaoEntity.class))).thenReturn(edicaoEntity);

        //ACT
        EdicaoDTO edicaoDTO = edicaoService.create(edicaoCreateDTO);

        //ASSERT
        assertNotNull(edicaoDTO);
        assertEquals(8, edicaoDTO.getIdEdicao());
    }

    //Nao funcional
    @Test
    public void testFindByIdSuccess() throws RegraDeNegocioException {
        // SETUP
        Integer id = 8;
        EdicaoEntity edicaoEntity = getEdicaoEntity();
        edicaoEntity.setIdEdicao(id);
        when(edicaoRepository.findById(anyInt())).thenReturn(Optional.of(edicaoEntity));

        // ACT
        EdicaoEntity edicao = edicaoService.findById(id);

        // ASSERT
        assertNotNull(edicao);
        assertEquals(8, edicaoEntity.getIdEdicao());

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

        Set<EtapaEntity> etapaEntities = new HashSet<>();
        etapaEntities.add(getEtapaEntity());
        edicaoEntity.setEtapas(etapaEntities);

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
        processoEntity.setAreaEnvolvida(getAreaEnvolvida());
        processoEntity.setResponsavel(getResponsavelEntity());

        return processoEntity;
    }

    private static ResponsavelEntity getResponsavelEntity() {
        ResponsavelEntity responsavelEntity = new ResponsavelEntity();
        responsavelEntity.setIdResponsavel(10);
        responsavelEntity.setResponsavel("Fulano");

        Set<ProcessoEntity> processoEntities = new HashSet<>();
        processoEntities.add(getProcessoEntity());
        responsavelEntity.setProcessos(processoEntities);

        return responsavelEntity;
    }

    private static AreaEnvolvidaEntity getAreaEnvolvida() {
        AreaEnvolvidaEntity areaEnvolvidaEntity = new AreaEnvolvidaEntity();
        areaEnvolvidaEntity.setAreaEnvolvida("Area1");
        areaEnvolvidaEntity.setIdAreaEnvolvida(10);

        Set<ProcessoEntity> processoEntities = new HashSet<>();
        processoEntities.add(getProcessoEntity());
        areaEnvolvidaEntity.setProcessos(processoEntities);

        return areaEnvolvidaEntity;
    }

}
