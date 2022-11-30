package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.dto.CalendarioDTO;
import br.com.dbc.chronosapi.entity.classes.EdicaoEntity;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalendarioService {

    private final EdicaoService edicaoService;

    public CalendarioDTO generate(Integer idEdicao) throws RegraDeNegocioException {
        EdicaoEntity edicaoEntity = edicaoService.findById(idEdicao);
        CalendarioDTO calendarioDTO = new CalendarioDTO();
        calendarioDTO.setDataInicial(edicaoEntity.getDataInicial());
        calendarioDTO.setDataFinal(edicaoEntity.getDataFinal());
        return calendarioDTO;
    }
}
