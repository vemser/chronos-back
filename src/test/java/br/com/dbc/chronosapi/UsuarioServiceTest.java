package br.com.dbc.chronosapi;

import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.processo.ProcessoCreateDTO;
import br.com.dbc.chronosapi.dto.usuario.CargoDTO;
import br.com.dbc.chronosapi.dto.usuario.UsuarioDTO;
import br.com.dbc.chronosapi.entity.classes.CargoEntity;
import br.com.dbc.chronosapi.entity.classes.EdicaoEntity;
import br.com.dbc.chronosapi.entity.classes.EtapaEntity;
import br.com.dbc.chronosapi.entity.classes.UsuarioEntity;
import br.com.dbc.chronosapi.entity.classes.processos.AreaEnvolvidaEntity;
import br.com.dbc.chronosapi.entity.classes.processos.ProcessoEntity;
import br.com.dbc.chronosapi.entity.classes.processos.ResponsavelEntity;
import br.com.dbc.chronosapi.entity.enums.Status;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.UsuarioRepository;
import br.com.dbc.chronosapi.service.UsuarioService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(usuarioService, "objectMapper", objectMapper);
    }

    @Test
    public void testListSucess(){
        // SETUP
        Integer pagina = 10;
        Integer quantidade = 5;

        UsuarioEntity usuarioEntity = getUsuarioEntity();

        UsuarioDTO usuarioDTO = getUsuarioDTO();

        Set<CargoEntity> cargos = new HashSet<>();
        cargos.add(getCargoEntity());

        Page<UsuarioEntity> paginaMock = new PageImpl<>(List.of(usuarioEntity));
        when(usuarioRepository.findAll(any(Pageable.class))).thenReturn(paginaMock);
//        when(objectMapper.convertValue(usuarioEntity, UsuarioDTO.class)).thenReturn(usuarioDTO);

        // ACT
        PageDTO<UsuarioDTO> paginaSolicitada = usuarioService.list(pagina, quantidade);

        // ASSERT
        assertNotNull(paginaSolicitada);
        assertNotNull(paginaSolicitada.getPagina());
        assertEquals(1, paginaSolicitada.getTotalElementos());
    }

    @Test
    public void testUploadImage() throws IOException, RegraDeNegocioException {

        // SETUP
        Integer usuarioID = 10;
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        usuarioEntity.setImagem(getMultipartedFile().getBytes());

        //ACT
        when(usuarioService.findById(usuarioID)).thenReturn(usuarioEntity);

        // ASSERT


    }

    private static MultipartFile getMultipartedFile(){
        MultipartFile multipartFile = new MultipartFile() {
            @Override
            public String getName() {
                return "Nome";
            }

            @Override
            public String getOriginalFilename() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

            }
        };
        return multipartFile;
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

    private static CargoEntity getCargoEntity(){
        CargoEntity cargoEntity = new CargoEntity();
        cargoEntity.setIdCargo(10);
        cargoEntity.setNome("ADMIN");
        cargoEntity.setDescricao("DESCRICAO");

        cargoEntity.setUsuarios(new HashSet<>());

        return cargoEntity;
    }

    private static UsuarioEntity getUsuarioEntity() {

        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setNome("User");
        usuarioEntity.setEmail("tadox76078@kuvasin.com");
        usuarioEntity.setIdUsuario(10);
        usuarioEntity.setStatus(Status.ATIVO);

        return usuarioEntity;
    }

    private static UsuarioDTO getUsuarioDTO(){

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario(10);
        usuarioDTO.setNome("User");
        usuarioDTO.setEmail("tadox76078@kuvasin.com");

        return usuarioDTO;

    }

    private static CargoDTO getCargoDTO(){

        CargoDTO cargoDTO = new CargoDTO();
        cargoDTO.setIdCargo(1);
        cargoDTO.setNome("ADMIN");
        cargoDTO.setDescricao("adm");

        return cargoDTO;

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
//        processoCreateDTO.setResponsavel(getResponsavelEntity());
//        processoCreateDTO.setAreaEnvolvida(getAreaEnvolvida());
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
        processoEntity.setOrdemExecucao(1);
        processoEntity.setDiasUteis(1);
//        processoEntity.setAreaEnvolvida(getAreaEnvolvida());
//        processoEntity.setResponsavel(getResponsavelEntity());

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
