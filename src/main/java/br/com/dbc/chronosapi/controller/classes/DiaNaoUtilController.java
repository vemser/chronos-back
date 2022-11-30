package br.com.dbc.chronosapi.controller.classes;


import br.com.dbc.chronosapi.controller.interfaces.DiaNaoUtilControllerInterface;
import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.diaNaoUtil.DiaNaoUtilCreateDTO;
import br.com.dbc.chronosapi.dto.diaNaoUtil.DiaNaoUtilDTO;
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
public class DiaNaoUtilController implements DiaNaoUtilControllerInterface {

    private final DiaNaoUtilService diaNaoUtilService;

    @GetMapping
    public ResponseEntity<PageDTO<DiaNaoUtilDTO>> list(Integer pagina, Integer tamanho) {
        return new ResponseEntity<>(diaNaoUtilService.list(pagina, tamanho), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DiaNaoUtilDTO> create(@Valid @RequestBody DiaNaoUtilCreateDTO diaNaoUtilCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(diaNaoUtilService.create(diaNaoUtilCreateDTO), HttpStatus.OK);
    }

    @PutMapping("/{idDiaNaoUtil}")
    public ResponseEntity<DiaNaoUtilDTO> update(@Valid @PathVariable ("idDiaNaoUtil") Integer idDiaNaoUtil,
                                                @Valid @RequestBody DiaNaoUtilCreateDTO diaNaoUtilCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(diaNaoUtilService.update(idDiaNaoUtil, diaNaoUtilCreateDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{idDiaNaoUtil}")
    public ResponseEntity<Void> delete(@Valid @PathVariable("idDiaNaoUtil") Integer idDiaNaoUtil) throws RegraDeNegocioException {
        diaNaoUtilService.delete(idDiaNaoUtil);
        return ResponseEntity.noContent().build();
    }
}
