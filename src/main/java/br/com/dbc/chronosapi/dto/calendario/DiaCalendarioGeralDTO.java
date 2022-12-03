package br.com.dbc.chronosapi.dto.calendario;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DiaCalendarioGeralDTO {
    private LocalDate dia;
    private DiaUtilDTO diaUtil;
    private List<JuncaoEdicoesDTO> edicoes;
}
