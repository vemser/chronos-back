package br.com.dbc.chronosapi.controller.classes;

import br.com.dbc.chronosapi.controller.interfaces.ProcessoControllerInterface;
import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.processo.ProcessoCreateDTO;
import br.com.dbc.chronosapi.dto.processo.ProcessoDTO;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.service.ProcessoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/processo")
public class ProcessoController implements ProcessoControllerInterface {

    private final ProcessoService processoService;
    @GetMapping
    public ResponseEntity<PageDTO<ProcessoDTO>> list(Integer pagina, Integer tamanho) throws RegraDeNegocioException {
        return ResponseEntity.ok(processoService.list(pagina, tamanho));
    }
    @GetMapping("/{idEtapa}")
    public ResponseEntity<List<ProcessoDTO>> listProcessosPorEtapa(@PathVariable("idEtapa") Integer idEtapa) throws RegraDeNegocioException {
        return new ResponseEntity<>(processoService.listProcessosDaEtapa(idEtapa), HttpStatus.OK);
    }
    @PostMapping("/{idEtapa}")
    public ResponseEntity<ProcessoDTO> create(@PathVariable("idEtapa") Integer idEtapa,
                                              @RequestBody ProcessoCreateDTO processoCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(processoService.create(idEtapa, processoCreateDTO), HttpStatus.OK);
    }
    @PutMapping("/{idProcesso}")
    public ResponseEntity<ProcessoDTO> update(@PathVariable("idProcesso") Integer idProcesso,
                                              @RequestBody ProcessoCreateDTO processoUpdate) throws RegraDeNegocioException {
        return new ResponseEntity<>(processoService.update(idProcesso, processoUpdate), HttpStatus.OK);
    }
    @DeleteMapping("/{idProcesso}")
    public ResponseEntity<Void> delete(@PathVariable("idProcesso") Integer idProcesso) throws RegraDeNegocioException {
        processoService.delete(idProcesso);
        return ResponseEntity.noContent().build();
    }
}
