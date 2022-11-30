package br.com.dbc.chronosapi.controller.classes;

import br.com.dbc.chronosapi.controller.interfaces.EtapaControllerInterface;
import br.com.dbc.chronosapi.dto.etapa.EtapaCreateDTO;
import br.com.dbc.chronosapi.dto.etapa.EtapaDTO;
import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.etapa.EtapaProcessoRelatorioDTO;
import br.com.dbc.chronosapi.entity.classes.processos.ProcessoEntity;
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
import java.util.Set;

@RequiredArgsConstructor
@Validated
@Slf4j
@RestController
@RequestMapping("/etapa")
public class EtapaController implements EtapaControllerInterface {

    private final EtapaService etapaService;

    @PostMapping("/{idEdicao}")
    public ResponseEntity<EtapaDTO> create(@PathVariable("idEdicao") Integer idEdicao,
                                           @Valid @RequestBody EtapaCreateDTO etapaCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(etapaService.create(idEdicao, etapaCreateDTO), HttpStatus.OK);
    }
    @GetMapping("/relatorio-etapa-processo")
    public ResponseEntity<Set<ProcessoEntity>> relatorioEtapaProcesso(@RequestParam(required = false)Integer idEtapa) {
        return new ResponseEntity<>(etapaService.listarRelatorioEtapaProcesso(idEtapa), HttpStatus.OK);
    }


    @PutMapping("/{idEtapa}")
    public ResponseEntity<EtapaDTO> update(@PathVariable ("idEtapa") Integer idEtapa,
                                           @Valid @RequestBody EtapaCreateDTO etapaCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(etapaService.update(idEtapa, etapaCreateDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{idEtapa}")
    public ResponseEntity<Void> delete(@PathVariable("idEtapa") Integer idEtapa) throws RegraDeNegocioException {
        etapaService.delete(idEtapa);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PageDTO<EtapaDTO>> list(Integer pagina, Integer tamanho) {
        return new ResponseEntity<>(etapaService.list(pagina, tamanho), HttpStatus.OK);
    }
}