package br.com.dbc.chronosapi.dto.diaNaoUtil;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FiltroDiaNaoUtilDTO {

    private String Descricao;
    private LocalDate dataInicial;
    private LocalDate dataFinal;
}
