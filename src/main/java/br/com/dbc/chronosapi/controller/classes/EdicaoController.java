package br.com.dbc.chronosapi.controller.classes;

import br.com.dbc.chronosapi.controller.interfaces.EdicaoControllerInterface;
import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.calendario.DiaCalendarioEdicaoDTO;
import br.com.dbc.chronosapi.dto.calendario.DiaCalendarioGeralDTO;
import br.com.dbc.chronosapi.dto.edicao.EdicaoCreateDTO;
import br.com.dbc.chronosapi.dto.edicao.EdicaoDTO;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.service.CalendarioExcelExporter;
import br.com.dbc.chronosapi.service.EdicaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    @GetMapping("/calendario-geral")
    public ResponseEntity <List<DiaCalendarioGeralDTO>> gerarCalendarioGeral() throws RegraDeNegocioException {
        return new ResponseEntity<>(edicaoService.gerarCalendarioGeral(), HttpStatus.OK);
    }

    @GetMapping("/calendario-edicao/{idEdicao}")
    public ResponseEntity<List<DiaCalendarioEdicaoDTO>> gerarCalendarioEdicao(@PathVariable Integer idEdicao) throws RegraDeNegocioException {
        return new ResponseEntity<>(edicaoService.gerarCalendarioEdicao(idEdicao, null), HttpStatus.OK);
    }

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
    public ResponseEntity<PageDTO<EdicaoDTO>> listComEtapa(Integer pagina, Integer tamanho) {
        return new ResponseEntity<>(edicaoService.listComEtapa(pagina, tamanho), HttpStatus.OK);
    }

    @GetMapping("/listar")
    public ResponseEntity<PageDTO<EdicaoDTO>> list(Integer pagina, Integer tamanho) throws RegraDeNegocioException {
        return new ResponseEntity<>(edicaoService.list(pagina, tamanho), HttpStatus.OK);
    }


    @GetMapping("/calendario/export/excel/{idEdicao}")
    public void exportToExcel(HttpServletResponse response, @PathVariable Integer idEdicao) throws IOException, RegraDeNegocioException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=calendario_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<DiaCalendarioEdicaoDTO> listCalendario = edicaoService.gerarCalendarioEdicao(idEdicao, null);

        CalendarioExcelExporter excelExporter = new CalendarioExcelExporter(listCalendario);

        excelExporter.export(response);
    }
}
