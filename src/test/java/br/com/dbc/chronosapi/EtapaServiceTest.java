package br.com.dbc.chronosapi;

import br.com.dbc.chronosapi.dto.EdicaoCreateDTO;
import br.com.dbc.chronosapi.dto.EdicaoDTO;
import br.com.dbc.chronosapi.dto.EtapaCreateDTO;
import br.com.dbc.chronosapi.dto.EtapaDTO;
import br.com.dbc.chronosapi.entity.classes.EdicaoEntity;
import br.com.dbc.chronosapi.entity.classes.EtapaEntity;
import br.com.dbc.chronosapi.entity.classes.processos.AreaEnvolvidaEntity;
import br.com.dbc.chronosapi.entity.classes.processos.ProcessoEntity;
import br.com.dbc.chronosapi.entity.classes.processos.ResponsavelEntity;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.EtapaRepository;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EtapaServiceTest {

    @InjectMocks
    private EtapaService etapaService;
    @Mock
    private EtapaRepository etapaRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(etapaService, "objectMapper", objectMapper);
    }

    @Test
    public void testCreateEtapaSuccess() {
        //SETUP
        EtapaCreateDTO etapaCreateDTO = getEtapaCreateDTO();
        EtapaEntity etapaEntity = getEtapaEntity();

        when(etapaRepository.save(any(EtapaEntity.class))).thenReturn(etapaEntity);

        //ACT
        EtapaDTO etapaDTO = etapaService.create(etapaCreateDTO);

        //ASSERT
        assertNotNull(etapaDTO);
        assertEquals(10, etapaDTO.getIdEtapa());
    }

    @Test
    public void testEdicaoUpdateSuccess() throws RegraDeNegocioException {

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