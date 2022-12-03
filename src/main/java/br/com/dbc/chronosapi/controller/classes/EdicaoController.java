package br.com.dbc.chronosapi.controller.classes;

import br.com.dbc.chronosapi.controller.interfaces.EdicaoControllerInterface;
import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.edicao.EdicaoCreateDTO;
import br.com.dbc.chronosapi.dto.edicao.EdicaoDTO;
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

public class EdicaoController implements EdicaoControllerInterface {

    private final EdicaoService edicaoService;

    @PostMapping()
    public ResponseEntity<EdicaoDTO> create(@Valid @RequestBody EdicaoCreateDTO edicaoCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(edicaoService.create(edicaoCreateDTO), HttpStatus.OK);
    }

    @PostMapping("/clone/{idEdicao}")
    public ResponseEntity<EdicaoDTO> clone(@Valid @PathVariable("idEdicao") Integer idEdicao) throws RegraDeNegocioException {
        return new ResponseEntity<>(edicaoService.clone(idEdicao), HttpStatus.OK);
    }

//    @GetMapping("/calendario/{idEdicao}")
//    public ResponseEntity<List<DiaDTO>> generate(@PathVariable Integer idEdicao) throws RegraDeNegocioException {
//        return new ResponseEntity<>(edicaoService.generate(idEdicao), HttpStatus.OK);
//    }

    @PutMapping("/{idEdicao}")
    public ResponseEntity<EdicaoDTO> update(@Valid @PathVariable ("idEdicao") Integer idEdicao,
                                            @Valid @RequestBody EdicaoCreateDTO edicaoCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(edicaoService.update(idEdicao, edicaoCreateDTO), HttpStatus.OK);
    }

    @PutMapping("/enable-disable/{idEdicao}")
    public ResponseEntity<Void> enableOrDisable(@Valid @PathVariable("idEdicao") Integer idEdicao) throws RegraDeNegocioException {
        edicaoService.enableOrDisable(idEdicao);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idEdicao}")
    public ResponseEntity<Void> delete(@Valid @PathVariable ("idEdicao") Integer idEdicao) throws RegraDeNegocioException {
        edicaoService.delete(idEdicao);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listar-com-etapa")
    public ResponseEntity<PageDTO<EdicaoDTO>> listComEtapa(Integer pagina, Integer tamanho) throws RegraDeNegocioException {
        return new ResponseEntity<>(edicaoService.listComEtapa(pagina, tamanho), HttpStatus.OK);
    }

    @GetMapping("/listar")
    public ResponseEntity<PageDTO<EdicaoDTO>> list(Integer pagina, Integer tamanho) throws RegraDeNegocioException {
        return new ResponseEntity<>(edicaoService.list(pagina, tamanho), HttpStatus.OK);
    }
}
