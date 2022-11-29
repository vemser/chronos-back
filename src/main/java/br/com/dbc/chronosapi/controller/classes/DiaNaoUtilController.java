package br.com.dbc.chronosapi.controller.classes;


import br.com.dbc.chronosapi.dto.DiaNaoUtilCreateDTO;
import br.com.dbc.chronosapi.dto.DiaNaoUtilDTO;
import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.service.DiaNaoUtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@Validated
@Slf4j
@RestController
@RequestMapping("/dia-nao-util")
public class DiaNaoUtilController {

    private final DiaNaoUtilService diaNaoUtilService;

    @PostMapping
    public ResponseEntity<DiaNaoUtilDTO> create(@Valid @RequestParam DiaNaoUtilCreateDTO diaNaoUtilCreateDTO) {
        return new ResponseEntity<>(diaNaoUtilService.create(diaNaoUtilCreateDTO), HttpStatus.OK);
    }

    @PutMapping("/{id-etapa}")


    @DeleteMapping("/{id-dia-nao-util}")
    public ResponseEntity<Void> delete(@Valid @PathVariable("id-dia-nao-util") Integer idDiaNaoUtil) throws RegraDeNegocioException {
        diaNaoUtilService.delete(idDiaNaoUtil);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listar-etapas")
    public ResponseEntity<PageDTO<DiaNaoUtilDTO>> list(Integer pagina, Integer tamanho) {
        return new ResponseEntity<>(diaNaoUtilService.list(pagina, tamanho), HttpStatus.OK);
    }

}
