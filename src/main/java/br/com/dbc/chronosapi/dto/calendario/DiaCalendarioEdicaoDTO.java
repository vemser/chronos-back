package br.com.dbc.chronosapi.dto.calendario;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DiaCalendarioEdicaoDTO {
    private LocalDate dia;
    private String etapa;
    private String processo;
    private List<String> areas;
    private String feriado;
}
