package br.com.dbc.chronosapi.controller.classes;

import br.com.dbc.chronosapi.controller.interfaces.AreaEnvolvidaControllerInterface;
import br.com.dbc.chronosapi.dto.processo.AreaEnvolvidaCreateDTO;
import br.com.dbc.chronosapi.dto.processo.AreaEnvolvidaDTO;
import br.com.dbc.chronosapi.dto.processo.ResponsavelDTO;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.service.AreaEnvolvidaService;
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
@RequestMapping("/areaEnvolvida")
public class AreaEnvolvidaController implements AreaEnvolvidaControllerInterface {
    private final AreaEnvolvidaService areaEnvolvidaService;

    @GetMapping
    public ResponseEntity<List<AreaEnvolvidaDTO>> listaAreas() {
        List<AreaEnvolvidaDTO> responsaveis = areaEnvolvidaService.listarAreas();
        return new ResponseEntity<>(responsaveis, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<AreaEnvolvidaDTO> create(@Valid @RequestBody AreaEnvolvidaCreateDTO areaEnvolvidaCreateDTO) {
        return new ResponseEntity<>(areaEnvolvidaService.create(areaEnvolvidaCreateDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{idAreaEnvolvida}")
    public ResponseEntity<Void> delete (@Valid @PathVariable("idAreaEnvolvida") Integer idAreaEnvolvida) throws RegraDeNegocioException {
        areaEnvolvidaService.delete(idAreaEnvolvida);
        return ResponseEntity.noContent().build();
    }
}
