package br.com.dbc.chronosapi.controller.classes;

import br.com.dbc.chronosapi.dto.CalendarioDTO;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.service.CalendarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Validated
@Slf4j
@RestController
@RequestMapping("/calendario")
public class CalendarioController {

    private final CalendarioService calendarioService;

    @PostMapping("/{idEdicao}")
    private CalendarioDTO generate(@RequestParam("idEdicao") Integer idEdicao) throws RegraDeNegocioException {
        return calendarioService.generate(idEdicao);
    }

}
