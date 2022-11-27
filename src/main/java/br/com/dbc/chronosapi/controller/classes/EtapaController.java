package br.com.dbc.chronosapi.controller.classes;

import br.com.dbc.chronosapi.controller.interfaces.IEtapaController;
import br.com.dbc.chronosapi.dto.EtapaCreateDTO;
import br.com.dbc.chronosapi.dto.EtapaDTO;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.service.EtapaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Validated
@Slf4j
@RestController
@RequestMapping("/etapa")
public class EtapaController implements IEtapaController {

    private final EtapaService etapaService;

    @PostMapping
    public ResponseEntity<EtapaDTO> create(@Valid @RequestParam EtapaCreateDTO etapaCreateDTO) {
        return new ResponseEntity<>(etapaService.create(etapaCreateDTO), HttpStatus.OK);
    }

    @PutMapping("/{id-etapa}")
    public ResponseEntity<EtapaDTO> update(@Valid @PathVariable ("id-etapa") Integer idEtapa,
                                           @Valid @RequestBody EtapaCreateDTO etapaCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(etapaService.update(idEtapa, etapaCreateDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id-etapa}")
    public ResponseEntity<Void> delete(@Valid @PathVariable("id-etapa") Integer idEtapa) throws RegraDeNegocioException {
        etapaService.delete(idEtapa);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listar-etapas")
    public ResponseEntity<List<EtapaDTO>> list() {
        return new ResponseEntity<>(etapaService.list(), HttpStatus.OK);
    }
}