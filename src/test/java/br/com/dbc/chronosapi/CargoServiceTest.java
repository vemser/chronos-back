//package br.com.dbc.chronosapi;
//
//import br.com.dbc.chronosapi.dto.edicao.EdicaoCreateDTO;
//import br.com.dbc.chronosapi.entity.classes.CargoEntity;
//import br.com.dbc.chronosapi.entity.classes.EdicaoEntity;
//import br.com.dbc.chronosapi.entity.classes.EtapaEntity;
//import br.com.dbc.chronosapi.entity.classes.processos.AreaEnvolvidaEntity;
//import br.com.dbc.chronosapi.entity.classes.processos.ProcessoEntity;
//import br.com.dbc.chronosapi.entity.classes.processos.ResponsavelEntity;
//import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
//import br.com.dbc.chronosapi.repository.CargoRepository;
//import br.com.dbc.chronosapi.service.CargoService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.time.LocalDate;
//import java.util.HashSet;
//import java.util.Optional;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class CargoServiceTest {
//
//    @InjectMocks
//    private CargoService cargoService;
//    @Mock
//    private CargoRepository cargoRepository;
//
//    @Test
//    public void testFindByIdSuccess() throws RegraDeNegocioException {
//        // SETUP
//        CargoEntity cargoEntity = getCargoEntity();
//        when(cargoRepository.findById(anyInt())).thenReturn(Optional.of(cargoEntity));
//
//        // ACT
//        CargoEntity cargo = cargoService.findById(cargoEntity.getIdCargo());
//
//        // ASSERT
//        assertNotNull(cargo);
//        assertEquals(10, cargoEntity.getIdCargo());
//
//    }
//
//    @Test
//    public void testFindByNome() throws RegraDeNegocioException {
//        // SETUP
//        CargoEntity cargoEntity = getCargoEntity();
//        when(cargoRepository.findByNome(anyString())).thenReturn(cargoEntity);
//
//        // ACT
//        CargoEntity cargo = cargoService.findByNome(cargoEntity.getNome());
//
//        // ASSERT
//        assertNotNull(cargo);
//        assertEquals("ADMIN", cargoEntity.getNome());
//
//    }
//
//    private CargoEntity getCargoEntity(){
//        CargoEntity cargoEntity = new CargoEntity();
//        cargoEntity.setIdCargo(10);
//        cargoEntity.setNome("ADMIN");
//        cargoEntity.setDescricao("DESCRICAO");
//
//        cargoEntity.setUsuarios(new HashSet<>());
//
//        return cargoEntity;
//    }
//
//    private EdicaoCreateDTO getEdicaoCreateDTO() {
//        EdicaoCreateDTO edicaoCreateDTO = new EdicaoCreateDTO();
//        edicaoCreateDTO.setNome("Edicao1");
//        edicaoCreateDTO.setDataInicial(LocalDate.of(2022,8,1));
//        edicaoCreateDTO.setDataFinal(LocalDate.of(2022,8,10));
//
//        return edicaoCreateDTO;
//    }
//    private static EdicaoEntity getEdicaoEntity() {
//
//        EdicaoEntity edicaoEntity = new EdicaoEntity();
//        edicaoEntity.setIdEdicao(10);
//        edicaoEntity.setNome("Edicao1");
//        edicaoEntity.setDataInicial(LocalDate.of(2022, 10, 11));
//        edicaoEntity.setDataFinal(LocalDate.of(2022, 12, 10));
//        edicaoEntity.setEtapas(new HashSet<>());
//
//        return edicaoEntity;
//    }
//
//    private static EtapaEntity getEtapaEntity() {
//        EtapaEntity etapaEntity = new EtapaEntity();
//        etapaEntity.setIdEtapa(2);
//        etapaEntity.setEdicao(getEdicaoEntity());
//        etapaEntity.setNome("Etapa1");
//
//        Set<ProcessoEntity> processoEntities = new HashSet<>();
//        processoEntities.add(getProcessoEntity());
//        etapaEntity.setProcessos(processoEntities);
//
//        return etapaEntity;
//    }
//
//    private static ProcessoEntity getProcessoEntity() {
//        ProcessoEntity processoEntity = new ProcessoEntity();
//        processoEntity.setIdProcesso(10);
//        processoEntity.setDuracaoProcesso("1dia");
//        processoEntity.setEtapa(getEtapaEntity());
//        processoEntity.setOrdemExecucao(1);
//        processoEntity.setDiasUteis(1);
//        processoEntity.setAreasEnvolvidas(new HashSet<>());
//        processoEntity.setResponsaveis(new HashSet<>());
//
//        return processoEntity;
//    }
//
//    private static ResponsavelEntity getResponsavelEntity() {
//        ResponsavelEntity responsavelEntity = new ResponsavelEntity();
//        responsavelEntity.setIdResponsavel(10);
//        responsavelEntity.setResponsavel("Fulano");
//
//        Set<ProcessoEntity> processoEntities = new HashSet<>();
//        processoEntities.add(getProcessoEntity());
//        responsavelEntity.setProcessos(processoEntities);
//
//        return responsavelEntity;
//    }
//
//    private static AreaEnvolvidaEntity getAreaEnvolvida() {
//        AreaEnvolvidaEntity areaEnvolvidaEntity = new AreaEnvolvidaEntity();
//        areaEnvolvidaEntity.setAreaEnvolvida("Area1");
//        areaEnvolvidaEntity.setIdAreaEnvolvida(10);
//
//        Set<ProcessoEntity> processoEntities = new HashSet<>();
//        processoEntities.add(getProcessoEntity());
//        areaEnvolvidaEntity.setProcessos(processoEntities);
//
//        return areaEnvolvidaEntity;
//    }
//
//}
