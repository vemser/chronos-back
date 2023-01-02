package br.com.dbc.chronosapi.dto.diaNaoUtil;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FiltroDiaNaoUtilDTO {

    private String descricao;
    private LocalDate dataInicial;
    private LocalDate dataFinal;
}
