package br.com.dbc.chronosapi.controller.classes;

import br.com.dbc.chronosapi.controller.interfaces.IEdicaoController;
import br.com.dbc.chronosapi.dto.EdicaoCreateDTO;
import br.com.dbc.chronosapi.dto.EdicaoDTO;
import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.service.EdicaoService;
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
@RequestMapping("/edicao")

public class EdicaoController implements IEdicaoController {

    private final EdicaoService edicaoService;

    @PostMapping()
    public ResponseEntity<EdicaoDTO> create(@Valid @RequestBody EdicaoCreateDTO edicaoCreateDTO) {
        return new ResponseEntity<>(edicaoService.create(edicaoCreateDTO), HttpStatus.OK);
    }

    @PutMapping("/{id-edicao}")
    public ResponseEntity<EdicaoDTO> update(@Valid @PathVariable ("id-edicao") Integer idEdicao,
                                            @Valid @RequestBody EdicaoCreateDTO edicaoCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(edicaoService.update(idEdicao, edicaoCreateDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id-edicao}")
    public ResponseEntity<Void> delete(@Valid @PathVariable ("id-edicao") Integer idEdicao) throws RegraDeNegocioException {
        edicaoService.delete(idEdicao);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listar-edicoes")
    public ResponseEntity<PageDTO<EdicaoDTO>> list(Integer pagina, Integer tamanho) throws RegraDeNegocioException {
        return new ResponseEntity<>(edicaoService.list(pagina, tamanho), HttpStatus.OK);
    }

}
