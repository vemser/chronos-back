//package br.com.dbc.chronosapi;
//
//import br.com.dbc.chronosapi.dto.processo.ResponsavelCreateDTO;
//import br.com.dbc.chronosapi.dto.processo.ResponsavelDTO;
//import br.com.dbc.chronosapi.entity.classes.processos.ResponsavelEntity;
//import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
//import br.com.dbc.chronosapi.repository.ResponsavelRepository;
//import br.com.dbc.chronosapi.service.ResponsavelService;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class ResponsavelServiceTest {
//    @InjectMocks
//    private ResponsavelService responsavelService;
//
//    @Mock
//    private ResponsavelRepository responsavelRepository;
//
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    @Before
//    public void init() {
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        ReflectionTestUtils.setField(responsavelService, "objectMapper", objectMapper);
//    }
//
//    @Test
//    public  void testCreateResponsavelSucess() {
//        // SETUP
//        ResponsavelCreateDTO responsavelCreateDTO = getResponsavelCreateDTO();
//        ResponsavelEntity responsavelEntity = getResponsavelEntity();
//        when(responsavelRepository.save(any(ResponsavelEntity.class))).thenReturn(responsavelEntity);
//        // ACT
//        ResponsavelDTO responsavelDTO = responsavelService.create(responsavelCreateDTO);
//        // ASSERT
//        assertNotNull(responsavelDTO);
//        assertEquals(10, responsavelDTO.getIdResponsavel());
//    }
//
//    @Test
//    public void testResponsavelDeleteSuccess() throws RegraDeNegocioException {
//        // SETUP
//        ResponsavelEntity responsavelEntity = getResponsavelEntity();
//
//        when(responsavelRepository.findById(anyInt())).thenReturn(Optional.of(responsavelEntity));
//
//        // ACT
//        responsavelService.delete(responsavelEntity.getIdResponsavel());
//
//        // ASSERT
//        verify(responsavelRepository, times(1)).delete(any());
//    }
//
//    @Test(expected = RegraDeNegocioException.class)
//    public void testResponsavelDeleteFail() throws RegraDeNegocioException {
//        // SETUP
//        when(responsavelRepository.findById(anyInt())).thenReturn(Optional.empty());
//
//        // ACT
//        responsavelService.delete(5);
//
//        // ASSERT
//        verify(responsavelRepository, times(1)).delete(any());
//    }
//
//    @Test
//    public void testFindByIdSuccess() throws RegraDeNegocioException {
//        // SETUP
//        ResponsavelEntity responsavelEntity = getResponsavelEntity();
//        when(responsavelRepository.findById(anyInt())).thenReturn(Optional.of(responsavelEntity));
//
//        // ACT
//        ResponsavelEntity responsavelEntity1 = responsavelService.findById(responsavelEntity.getIdResponsavel());
//
//        // ASSERT
//        assertNotNull(responsavelEntity1);
//        assertEquals(10, responsavelEntity1.getIdResponsavel());
//
//    }
//
//    @Test
//    public void testFindByNome() throws RegraDeNegocioException {
//        // SETUP
//        ResponsavelEntity responsavelEntity = getResponsavelEntity();
//        when(responsavelRepository.findByNomeResponsavel(anyString())).thenReturn(responsavelEntity);
//
//        // ACT
//        ResponsavelEntity responsavelEntity1 = responsavelService.findByNomeResponsavel(responsavelEntity.getNome());
//
//        // ASSERT
//        assertNotNull(responsavelEntity1);
//        assertEquals("Fulano", responsavelEntity1.getNome());
//
//    }
//
//    @Test
//    public void testFindByNomeContainingIgnoreCase() throws RegraDeNegocioException {
//        // SETUP
//        ResponsavelEntity responsavelEntity = getResponsavelEntity();
//        when(responsavelRepository.findByNomeResponsavel(anyString())).thenReturn(responsavelEntity);
//
//        // ACT
//        ResponsavelEntity responsavelEntity1 = responsavelService.findByNomeResponsavel(responsavelEntity.getNome());
//
//        // ASSERT
//        assertNotNull(responsavelEntity1);
//        assertEquals("Fulano", responsavelEntity1.getNome());
//
//    }
//
//    @Test
//    public void testListResponsaveis(){
//        // SETUP
//        List<ResponsavelEntity> lista = new ArrayList<>();
//        lista.add(getResponsavelEntity());
//        when(responsavelRepository.findAll()).thenReturn(lista);
//
//        //(ACT)
//        List<ResponsavelDTO> listaDto = responsavelService.listarResponsaveis();
//
//        // (ASSERT)
//        assertNotNull(listaDto);
//        assertTrue(listaDto.size() > 0);
//        assertEquals(1, lista.size());
//    }
//
//
//    private static ResponsavelEntity getResponsavelEntity() {
//        ResponsavelEntity responsavelEntity = new ResponsavelEntity();
//        responsavelEntity.setIdResponsavel(10);
//        responsavelEntity.setNome("Fulano");
//        responsavelEntity.setProcessos(new HashSet<>());
//
//        return responsavelEntity;
//    }
//
//    private static ResponsavelDTO getResponsavelDTO() {
//        ResponsavelDTO responsavelDTO = new ResponsavelDTO();
//        responsavelDTO.setIdResponsavel(10);
//        responsavelDTO.setNome("Fulano");
//
//        return responsavelDTO;
//    }
//
//    private static ResponsavelCreateDTO getResponsavelCreateDTO() {
//        ResponsavelCreateDTO responsavelCreateDTO = new ResponsavelCreateDTO();
//        responsavelCreateDTO.setNome("Fulano");
//        return responsavelCreateDTO;
//    }
//}
