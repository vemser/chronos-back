package br.com.dbc.chronosapi;

import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.calendario.DiaCalendarioEdicaoDTO;
import br.com.dbc.chronosapi.dto.calendario.DiaCalendarioGeralDTO;
import br.com.dbc.chronosapi.dto.calendario.FeriadoDTO;
import br.com.dbc.chronosapi.dto.edicao.EdicaoCreateDTO;
import br.com.dbc.chronosapi.dto.edicao.EdicaoDTO;
import br.com.dbc.chronosapi.dto.etapa.EtapaDTO;
import br.com.dbc.chronosapi.dto.processo.AreaEnvolvidaDTO;
import br.com.dbc.chronosapi.dto.processo.ProcessoDTO;
import br.com.dbc.chronosapi.dto.processo.ResponsavelDTO;
import br.com.dbc.chronosapi.entity.classes.DiaNaoUtilEntity;
import br.com.dbc.chronosapi.entity.classes.EdicaoEntity;
import br.com.dbc.chronosapi.entity.classes.EtapaEntity;
import br.com.dbc.chronosapi.entity.classes.processos.AreaEnvolvidaEntity;
import br.com.dbc.chronosapi.entity.classes.processos.ProcessoEntity;
import br.com.dbc.chronosapi.entity.classes.processos.ResponsavelEntity;
import br.com.dbc.chronosapi.entity.enums.Status;
import br.com.dbc.chronosapi.repository.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.DiaNaoUtilRepository;
import br.com.dbc.chronosapi.repository.EdicaoRepository;
import br.com.dbc.chronosapi.repository.EtapaRepository;
import br.com.dbc.chronosapi.repository.ProcessoRepository;
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
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.*;

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
    @Mock
    private EtapaRepository etapaRepository;
    @Mock
    private ProcessoRepository processoRepository;
    @Mock
    private DiaNaoUtilRepository diaNaoUtilRepository;
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

        when(edicaoRepository.save(any(EdicaoEntity.class))).thenReturn(edicaoEntity);

        //ACT
        EdicaoDTO edicaoDTO = edicaoService.create(edicaoCreateDTO);

        //ASSERT
        assertNotNull(edicaoDTO);
        assertEquals(10, edicaoDTO.getIdEdicao());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void testCreateEdicaoFail() throws RegraDeNegocioException {
        //SETUP
        EdicaoCreateDTO edicaoCreateDTO = getEdicaoCreateDTO();
        EdicaoEntity edicaoEntity = getEdicaoEntity();
        edicaoCreateDTO.setDataInicial(LocalDate.parse("2022-12-25"));
        edicaoCreateDTO.setDataFinal(LocalDate.parse("1900-12-25"));

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

    @Test(expected = RegraDeNegocioException.class)
    public void testEdicaoUpdateFail() throws RegraDeNegocioException {

        // SETUP
        EdicaoCreateDTO edicaoCreateDTO = getEdicaoCreateDTO();

        EdicaoEntity edicaoEntity = getEdicaoEntity();
        EdicaoEntity edicaoEntity1 = getEdicaoEntity();
        edicaoCreateDTO.setDataInicial(LocalDate.parse("2022-12-25"));
        edicaoCreateDTO.setDataFinal(LocalDate.parse("1900-12-25"));
        edicaoEntity1.setNome("nomeDiferente");

        when(edicaoRepository.findById(anyInt())).thenReturn(Optional.of(edicaoEntity));

        // ACT
        EdicaoDTO edicaoDTO = edicaoService.update(edicaoEntity.getIdEdicao(), edicaoCreateDTO);

        // ASSERT
        assertNotNull(edicaoDTO);
        assertNotEquals("nomeDiferente", edicaoDTO.getNome());
    }
    @Test
    public void testCloneSucess() throws RegraDeNegocioException {
        // teste edicao
        EdicaoEntity edicaoEntity = getEdicaoEntity();

        List<EtapaEntity> etapaEntities = new ArrayList<>();
        etapaEntities.add(getEtapaEntity());
        etapaEntities.add(getEtapaEntity2());

        edicaoEntity.setEtapas(etapaEntities);
        EdicaoEntity edicaoEntityClone = new EdicaoEntity();
        edicaoEntityClone.setNome(edicaoEntity.getNome() + " - Clone");
        edicaoEntityClone.setIdEdicao(12);
        edicaoEntityClone.setStatus(Status.INATIVO);
        edicaoEntityClone.setDataInicial(edicaoEntity.getDataInicial());
        edicaoEntityClone.setDataFinal(edicaoEntity.getDataFinal());
        when(edicaoRepository.findById(anyInt())).thenReturn(Optional.of(edicaoEntity));
        when(edicaoRepository.save(any())).thenReturn(edicaoEntityClone);

        List<EtapaEntity> etapaEntity = edicaoEntityClone.getEtapas();
        EtapaEntity etapaEntityClone = new EtapaEntity();
        when(etapaRepository.save(any())).thenReturn(etapaEntityClone);

        Set<ProcessoEntity> processoEntity = etapaEntityClone.getProcessos();
        ProcessoEntity processoEntityClone = new ProcessoEntity();

        when(processoRepository.save(any())).thenReturn(processoEntityClone);

        EdicaoDTO edicaoDTO = edicaoService.clone(edicaoEntity.getIdEdicao());


        assertNotNull(edicaoDTO);
        assertEquals(12, edicaoDTO.getIdEdicao());
    }
    @Test(expected = RegraDeNegocioException.class)
    public void testGerarCalendarioEdicaoIfEtapasEmpty() throws RegraDeNegocioException {
        List<DiaNaoUtilEntity> diaNaoUtilEntityList = new ArrayList<>();
        diaNaoUtilEntityList.add(getDiaNaoUtilEntity());
        EdicaoEntity edicaoEntity = getEdicaoEntity();
        Set<EtapaEntity> etapaEntities = new HashSet<>();

        Map<String, String> coresPorEtapa = new HashMap<>();
        coresPorEtapa = null;

        when(edicaoRepository.findById(anyInt())).thenReturn(Optional.of(edicaoEntity));

        edicaoService.gerarCalendarioEdicao(edicaoEntity.getIdEdicao(), coresPorEtapa);
    }

    @Test
    public void testListComEtapaSucess(){
        // SETUP
        Integer pagina = 10;
        Integer quantidade = 5;

        EdicaoEntity edicaoEntity = getEdicaoEntity();
        Page<EdicaoEntity> paginaMock = new PageImpl<>(List.of(edicaoEntity));
        when(edicaoRepository.findAll(any(Pageable.class))).thenReturn(paginaMock);

        // ACT
        PageDTO<EdicaoDTO> paginaSolicitada = edicaoService.listComEtapa(pagina, quantidade);

        // ASSERT
        assertNotNull(paginaSolicitada);
        assertNotNull(paginaSolicitada.getPagina());
        assertEquals(1, paginaSolicitada.getTotalElementos());
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
    public void testEnableOrDisableWhenInativo() throws RegraDeNegocioException {

        //SET
        Integer idEdicao = 10;
        EdicaoEntity edicaoEntity = getEdicaoEntity();

        when(edicaoRepository.findById(anyInt())).thenReturn(Optional.of(edicaoEntity));
        // ACT
        edicaoService.enableOrDisable(edicaoEntity.getIdEdicao());

        //ASSERT
        verify(edicaoRepository, times(1)).save(any());
    }
    @Test
    public void testEnableOrDisableWhenAtivo() throws RegraDeNegocioException {

        //SET
        Integer idEdicao = 10;
        EdicaoEntity edicaoEntity = getEdicaoEntity();
        edicaoEntity.setStatus(Status.ATIVO);
        when(edicaoRepository.findById(anyInt())).thenReturn(Optional.of(edicaoEntity));
        // ACT
        edicaoService.enableOrDisable(edicaoEntity.getIdEdicao());

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

    @Test
    public void testGetResponsavelDTOSuccess(){

        Set<ResponsavelEntity> responsavelEntities = new HashSet<>();
        ResponsavelEntity responsavelEntity = getResponsavelEntity();
        responsavelEntities.add(responsavelEntity);

        Set<ResponsavelDTO> responsavelDTOS = edicaoService.getResponsavelDTO(responsavelEntities);

        assertNotNull(responsavelDTOS);

    }
    @Test
    public void testGetAreaEnvolvidaDTOSuccess(){

        Set<AreaEnvolvidaEntity> areaEnvolvidaEntities = new HashSet<>();
        AreaEnvolvidaEntity areaEnvolvidaEntity = getAreaEnvolvida();
        areaEnvolvidaEntities.add(areaEnvolvidaEntity);

        Set<AreaEnvolvidaDTO> areaEnvolvidaDTOS = edicaoService.getAreaEnvolvidaDTO(areaEnvolvidaEntities);

        assertNotNull(areaEnvolvidaDTOS);

    }
    @Test
    public void testGerarCalendarioEdicaoElse() throws RegraDeNegocioException {

        final int UM_DIA = 1;
        List<EdicaoEntity> edicaoEntityList = new ArrayList<>();
        edicaoEntityList.add(getEdicaoEntity());
        edicaoEntityList.add(getEdicaoEntity2());

        when(edicaoRepository.findByEdicoesAtivasOrderByDataInicial()).thenReturn(edicaoEntityList);

        List<DiaCalendarioEdicaoDTO> diaCalendarioEdicaoDTOList = new ArrayList<>();
        diaCalendarioEdicaoDTOList.add(getDiaCalendarioEdicaoDTO());

        List<DiaNaoUtilEntity> diaNaoUtilEntityList = new ArrayList<>();
        diaNaoUtilEntityList.add(getDiaNaoUtilEntity());

        EdicaoEntity edicaoEntity = getEdicaoEntity();
        when(edicaoRepository.findById(anyInt())).thenReturn(Optional.of(edicaoEntity));

        List<EtapaEntity> etapaEntities = new ArrayList<>();
        etapaEntities.add(getEtapaEntity());
        etapaEntities.add(getEtapaEntity2());

        edicaoEntity.setEtapas(etapaEntities);

        FeriadoDTO feriadoDTO = getFeriadoDTO();
        feriadoDTO.setQtdDias(13);



        List<DiaNaoUtilEntity> diaNaoUtilEntityList2 = new ArrayList<>();
        diaNaoUtilEntityList2.add(getDiaNaoUtilEntity());
        diaNaoUtilEntityList2.add(getDiaNaoUtilEntityInativo());

        feriadoDTO.setQtdDias(10);

        List<DiaCalendarioGeralDTO> diaCalendarioGeralDTOS = edicaoService.gerarCalendarioGeral();

    }
    @Test
    public void testGerarCalendarioGeral() throws RegraDeNegocioException {
        final int UM_DIA = 1;
        List<EdicaoEntity> edicaoEntityList = new ArrayList<>();
        edicaoEntityList.add(getEdicaoEntity());
        edicaoEntityList.add(getEdicaoEntity2());

        List<EtapaEntity> etapaEntities = new ArrayList<>();
        etapaEntities.add(getEtapaEntity());
        etapaEntities.add(getEtapaEntityComProcessoComAreas());
        etapaEntities.add(getEtapaEntity2());

        edicaoEntityList.get(0).setEtapas(etapaEntities);

        EdicaoEntity edicaoEntity = getEdicaoEntity();
        edicaoEntity.setEtapas(etapaEntities);

        List<DiaCalendarioEdicaoDTO> diaCalendarioEdicaoDTOList = new ArrayList<>();
        diaCalendarioEdicaoDTOList.add(getDiaCalendarioEdicaoDTO());

        when(edicaoRepository.findByEdicoesAtivasOrderByDataInicial()).thenReturn(edicaoEntityList);
        when(edicaoRepository.findById(anyInt())).thenReturn(Optional.of(edicaoEntity));

        FeriadoDTO feriadoDTO = getFeriadoDTO();

        LocalDate dia = LocalDate.parse("2022-03-03");

        Map<String, String> coresPorEtapa = new HashMap<>();
        EtapaEntity etapaEntity = getEtapaEntity();
        Set<String> nomesEtapas = new HashSet<>();
        nomesEtapas.add(etapaEntity.getNome());

        List<DiaNaoUtilEntity> diaNaoUtilEntityList = new ArrayList<>();
        DiaNaoUtilEntity diaNaoUtilEntity = getDiaNaoUtilEntity();
        diaNaoUtilEntity.setDataInicial(LocalDate.parse("2022-10-11"));
        diaNaoUtilEntityList.add(diaNaoUtilEntity);
        diaNaoUtilEntityList.add(getDiaNaoUtilEntityInativo());
        when(diaNaoUtilRepository.findAll(any(Sort.class))).thenReturn(diaNaoUtilEntityList);


        List<DiaCalendarioGeralDTO> diaCalendarioGeralDTOS = edicaoService.gerarCalendarioGeral();

        assertNotNull(diaCalendarioGeralDTOS);

    }

    @Test(expected = RegraDeNegocioException.class)
    public void testGerarCalendarioGeralFail() throws RegraDeNegocioException {
        List<EdicaoEntity> edicaoEntityList = new ArrayList<>();

        when(edicaoRepository.findByEdicoesAtivasOrderByDataInicial()).thenReturn(edicaoEntityList);

        edicaoService.gerarCalendarioGeral();

    }
    @Test
    public void testVerificarDiasNaoUteisElseIf() {
        FeriadoDTO feriado = new FeriadoDTO();
        LocalDate dia = LocalDate.parse("2012-11-11");
        List<DiaNaoUtilEntity> diaNaoUtilEntityList = new ArrayList<>();
        DiaNaoUtilEntity diaNaoUtilEntity = getDiaNaoUtilEntity();
        diaNaoUtilEntity.setRepeticaoAnual(Status.ATIVO);
        diaNaoUtilEntity.setDataInicial(dia);

        FeriadoDTO feriadoReturn = edicaoService.verificarDiasNaoUteis(dia, diaNaoUtilEntityList);

        assertNotNull(feriadoReturn);

    }
    @Test
    public void testOrganizarCoresSuccess() {
        List<EtapaEntity> etapas = new ArrayList<>();
        etapas.add(getEtapaEntity2());
        etapas.add(getEtapaEntity());


        List<EtapaEntity> etapas2 = new ArrayList<>();
        etapas2.add(getEtapaEntity());
        etapas2.add(getEtapaEntity2());
        EtapaEntity etapaEntity = getEtapaEntity();
        etapaEntity.setNome("ABC");
        etapaEntity.setOrdemExecucao(3);
        etapas2.add(etapaEntity);

        List<EtapaEntity> novaListaEtapas = new ArrayList<>();
        novaListaEtapas.addAll(etapas);
        novaListaEtapas.addAll(etapas2);

        Map<String, String> map = edicaoService.organizarCores(novaListaEtapas);

        assertNotNull(map);
        assertEquals("#ef4444",map.get("Etapa1"));
        assertEquals("#3b82f6",map.get("Etapa2"));
        assertEquals("#84cc16",map.get("ABC"));
    }

    private static FeriadoDTO getFeriadoDTO(){
        FeriadoDTO feriadoDTO = new FeriadoDTO();
        feriadoDTO.setDescricao("Dia do saci");
        feriadoDTO.setQtdDias(3);
        return feriadoDTO;
    }

    private static DiaNaoUtilEntity getDiaNaoUtilEntityInativo() {
        DiaNaoUtilEntity diaNaoUtilEntity = new DiaNaoUtilEntity();
        diaNaoUtilEntity.setRepeticaoAnual(Status.INATIVO);
        diaNaoUtilEntity.setIdDiaNaoUtil(3);
        diaNaoUtilEntity.setDataInicial(LocalDate.parse("2022-10-12"));
        diaNaoUtilEntity.setDataFinal(LocalDate.parse("2022-12-08"));
        diaNaoUtilEntity.setDescricao("Diazinho de cria tlg HIHIHI");

        return diaNaoUtilEntity;
    }

    private static DiaNaoUtilEntity getDiaNaoUtilEntity() {
        DiaNaoUtilEntity diaNaoUtilEntity = new DiaNaoUtilEntity();
        diaNaoUtilEntity.setRepeticaoAnual(Status.ATIVO);
        diaNaoUtilEntity.setIdDiaNaoUtil(3);
        diaNaoUtilEntity.setDataInicial(LocalDate.parse("2022-03-03"));
        diaNaoUtilEntity.setDataFinal(null);
        diaNaoUtilEntity.setDescricao("Diazinho de cria tlg");

        return diaNaoUtilEntity;
    }

    private static DiaCalendarioEdicaoDTO getDiaCalendarioEdicaoDTO() {
        DiaCalendarioEdicaoDTO diaCalendarioEdicaoDTO = new DiaCalendarioEdicaoDTO();
        diaCalendarioEdicaoDTO.setDia(LocalDate.parse("2022-03-03"));
//        diaCalendarioEdicaoDTO.setDiaUtil(getDiaUtilDTO());
//        diaCalendarioEdicaoDTO.setEtapa(getEtapaDTO());
//        diaCalendarioEdicaoDTO.setProcesso(getprocessoDTO());
        return diaCalendarioEdicaoDTO;
    }

    private static DiaCalendarioGeralDTO getDiaCalendarioGeralDTO() {
        List<EdicaoEntity> edicaoEntities = new ArrayList<>();
        edicaoEntities.add(getEdicaoEntity());

        DiaCalendarioGeralDTO diaCalendarioGeralDTO = new DiaCalendarioGeralDTO();
//        diaCalendarioGeralDTO.setDiaUtil(getDiaUtilDTO());
//        diaCalendarioGeralDTO.setEdicoes(juncaoEdicoesDTOS);
        diaCalendarioGeralDTO.setDia(LocalDate.parse("2022-03-03"));

        return diaCalendarioGeralDTO;
    }

    private static EtapaDTO getEtapaDTO() {

        List<ProcessoDTO> processoDTOS = new ArrayList<>();
        processoDTOS.add(getprocessoDTO());

        EtapaDTO etapaDTO = new EtapaDTO();
        etapaDTO.setNome("Etapa1");
        etapaDTO.setIdEtapa(3);
        etapaDTO.setOrdemExecucao(3);
        etapaDTO.setProcessos(processoDTOS);

        return etapaDTO;
    }

    private static ProcessoDTO getprocessoDTO() {
        Set<ResponsavelDTO> responsavelDTOS = new HashSet<>();
        responsavelDTOS.add(getResponsavelDTO());

        Set<AreaEnvolvidaDTO> areaEnvolvidaDTOS = new HashSet<>();
        areaEnvolvidaDTOS.add(getAreaEnvolvidaDTO());

        ProcessoDTO processoDTO = new ProcessoDTO();
        processoDTO.setDiasUteis(3);
        processoDTO.setDuracaoProcesso("3 dias");
        processoDTO.setIdProcesso(3);
        processoDTO.setOrdemExecucao(3);
        processoDTO.setResponsaveis(responsavelDTOS);
        processoDTO.setAreasEnvolvidas(areaEnvolvidaDTOS);
        processoDTO.setNome("processo 3");

        return processoDTO;
    }
    private static AreaEnvolvidaDTO getAreaEnvolvidaDTO() {
        AreaEnvolvidaDTO areaEnvolvidaDTO = new AreaEnvolvidaDTO();
        areaEnvolvidaDTO.setIdAreaEnvolvida(10);
        areaEnvolvidaDTO.setNome("area1");
        areaEnvolvidaDTO.setIdAreaEnvolvida(10);
        return areaEnvolvidaDTO;
    }

    private static ResponsavelDTO getResponsavelDTO() {
        ResponsavelDTO responsavelDTO = new ResponsavelDTO();
        responsavelDTO.setIdResponsavel(10);
        responsavelDTO.setNome("Fulano");

        return responsavelDTO;
    }

    private static EdicaoCreateDTO getEdicaoCreateDTO() {
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
        edicaoEntity.setEtapas(new ArrayList<>());

        return edicaoEntity;
    }

    private static EdicaoEntity getEdicaoEntity2() {

        EdicaoEntity edicaoEntity = new EdicaoEntity();
        edicaoEntity.setIdEdicao(11);
        edicaoEntity.setNome("Edicao2");
        edicaoEntity.setDataInicial(LocalDate.of(2022, 11, 11));
        edicaoEntity.setDataFinal(LocalDate.of(2022, 12, 10));
        edicaoEntity.setEtapas(new ArrayList<>());

        return edicaoEntity;
    }

    private static EtapaEntity getEtapaEntity() {
        EtapaEntity etapaEntity = new EtapaEntity();
        etapaEntity.setIdEtapa(2);
        etapaEntity.setOrdemExecucao(1);
        etapaEntity.setNome("Etapa1");

        Set<ProcessoEntity> processoEntities = new HashSet<>();
        processoEntities.add(getProcessoEntity());
        processoEntities.add(getProcessoEntity2());
        etapaEntity.setProcessos(processoEntities);

        return etapaEntity;
    }

    private static EtapaEntity getEtapaEntityComProcessoComAreas() {
        EtapaEntity etapaEntity = new EtapaEntity();
        etapaEntity.setIdEtapa(2);
        etapaEntity.setOrdemExecucao(3);
        etapaEntity.setNome("Etapa1");

        Set<ProcessoEntity> processoEntities = new HashSet<>();
        processoEntities.add(getProcessoEntityComAreas());
        processoEntities.add(getProcessoEntity2());
        etapaEntity.setProcessos(processoEntities);

        return etapaEntity;
    }

    private static EtapaEntity getEtapaEntity2() {
        EtapaEntity etapaEntity = new EtapaEntity();
        etapaEntity.setIdEtapa(3);
        etapaEntity.setNome("Etapa2");
        etapaEntity.setOrdemExecucao(2);

        Set<ProcessoEntity> processoEntities = new HashSet<>();
        processoEntities.add(getProcessoEntity());
        processoEntities.add(getProcessoEntity2());
        etapaEntity.setProcessos(processoEntities);

        return etapaEntity;
    }
    private static ProcessoEntity getProcessoEntity() {
        ProcessoEntity processoEntity = new ProcessoEntity();
        processoEntity.setIdProcesso(10);
        processoEntity.setDuracaoProcesso("1dia");
        processoEntity.setOrdemExecucao(1);
        processoEntity.setDiasUteis(7);
        processoEntity.setAreasEnvolvidas(new HashSet<>());

        processoEntity.setResponsaveis(new HashSet<>());

        return processoEntity;
    }

    private static ProcessoEntity getProcessoEntityComAreas() {
        ProcessoEntity processoEntity = new ProcessoEntity();
        processoEntity.setIdProcesso(10);
        processoEntity.setDuracaoProcesso("1dia");
        processoEntity.setOrdemExecucao(1);
        processoEntity.setDiasUteis(7);
        Set<AreaEnvolvidaEntity> areaEnvolvidaEntities = new HashSet<>();
        areaEnvolvidaEntities.add(getAreaEnvolvida());
        processoEntity.setAreasEnvolvidas(areaEnvolvidaEntities);

        processoEntity.setResponsaveis(new HashSet<>());

        return processoEntity;
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
