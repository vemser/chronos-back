package br.com.dbc.chronosapi.dto.calendario;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DiaCalendarioGeralDTO {
    private LocalDate dia;
    private String edicao;
    private Integer idEtapa;
    private String etapa;
    private String feriado;
}
