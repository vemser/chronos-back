package br.com.dbc.chronosapi.controller.classes;

import br.com.dbc.chronosapi.controller.interfaces.ResponsavelControllerInterface;
import br.com.dbc.chronosapi.dto.processo.ResponsavelCreateDTO;
import br.com.dbc.chronosapi.dto.processo.ResponsavelDTO;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.service.ResponsavelService;
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
@RequestMapping("/responsavel")
public class ResponsavelController implements ResponsavelControllerInterface {
    private final ResponsavelService responsavelService;

    @GetMapping
    public ResponseEntity<List<ResponsavelDTO>> listResponsaveis() {
        List<ResponsavelDTO> responsaveis = responsavelService.listarResponsaveis();
        return new ResponseEntity<>(responsaveis, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<ResponsavelDTO> create(@Valid @RequestBody ResponsavelCreateDTO responsavelCreateDTO) {
        return new ResponseEntity<>(responsavelService.create(responsavelCreateDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{idResponsavel}")
    public ResponseEntity<Void> delete (@Valid @PathVariable("idResponsavel") Integer idResponsavel) throws RegraDeNegocioException {
        responsavelService.delete(idResponsavel);
        return ResponseEntity.noContent().build();
    }

}
