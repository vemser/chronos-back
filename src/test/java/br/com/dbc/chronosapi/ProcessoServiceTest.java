package br.com.dbc.chronosapi;

import br.com.dbc.chronosapi.dto.processo.ProcessoCreateDTO;
import br.com.dbc.chronosapi.dto.processo.ProcessoDTO;
import br.com.dbc.chronosapi.entity.classes.EdicaoEntity;
import br.com.dbc.chronosapi.entity.classes.EtapaEntity;
import br.com.dbc.chronosapi.entity.classes.processos.AreaEnvolvidaEntity;
import br.com.dbc.chronosapi.entity.classes.processos.ProcessoEntity;
import br.com.dbc.chronosapi.entity.classes.processos.ResponsavelEntity;
import br.com.dbc.chronosapi.repository.ProcessoRepository;
import br.com.dbc.chronosapi.service.ProcessoService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProcessoServiceTest {

    @InjectMocks
    private ProcessoService processoService;

    @Mock
    private ProcessoRepository processoRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(processoService, "objectMapper", objectMapper);
    }

    @Test
    public void testCreateProcessoSuccess() {
        //SETUP
        ProcessoCreateDTO processoCreateDTO = getProcessoCreateDTO();
        ProcessoEntity processoEntity = getProcessoEntity();

        when(processoRepository.save(any(ProcessoEntity.class))).thenReturn(processoEntity);

        //ACT
        ProcessoDTO processoDTO = processoService.create(processoCreateDTO);

        //ASSERT
        assertNotNull(processoDTO);
        assertEquals(10, processoDTO.getIdProcesso());
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
        processoCreateDTO.setResponsavel(getResponsavelEntity());
        processoCreateDTO.setAreaEnvolvida(getAreaEnvolvida());
        processoCreateDTO.setOrdemExecucao(10);
        processoCreateDTO.setDuracaoProcesso("1 dia");
        processoCreateDTO.setDiasUteis(1);

        return processoCreateDTO;
    }
    private static ProcessoEntity getProcessoEntity() {
        ProcessoEntity processoEntity = new ProcessoEntity();
        processoEntity.setIdProcesso(10);
        processoEntity.setDuracaoProcesso("1 dia");
        processoEntity.setEtapa(getEtapaEntity());
        processoEntity.setEdicoes(new HashSet<>());
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
        responsavelEntity.setProcessos(new HashSet<>());

        return responsavelEntity;
    }

    private static AreaEnvolvidaEntity getAreaEnvolvida() {
        AreaEnvolvidaEntity areaEnvolvidaEntity = new AreaEnvolvidaEntity();
        areaEnvolvidaEntity.setAreaEnvolvida("Area1");
        areaEnvolvidaEntity.setIdAreaEnvolvida(10);

        areaEnvolvidaEntity.setProcessos(new HashSet<>());

        return areaEnvolvidaEntity;
    }

}
