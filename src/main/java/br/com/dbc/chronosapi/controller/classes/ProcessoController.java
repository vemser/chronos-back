package br.com.dbc.chronosapi.controller.classes;

import br.com.dbc.chronosapi.controller.interfaces.IProcessoController;
import br.com.dbc.chronosapi.dto.ProcessoCreateDTO;
import br.com.dbc.chronosapi.dto.ProcessoDTO;
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
public class ProcessoController implements IProcessoController {

    private final ProcessoService processoService;
    @GetMapping()
    public ResponseEntity<List<ProcessoDTO>> list() {
        return new ResponseEntity<>(processoService.list(), HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<ProcessoDTO> create(@RequestBody ProcessoCreateDTO processoCreateDTO) {
        return new ResponseEntity<>(processoService.create(processoCreateDTO), HttpStatus.OK);
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
