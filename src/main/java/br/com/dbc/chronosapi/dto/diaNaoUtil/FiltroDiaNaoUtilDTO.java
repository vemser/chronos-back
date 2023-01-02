package br.com.dbc.chronosapi.dto.diaNaoUtil;

import lombok.Data;

import java.util.Date;

@Data
public class FiltroDiaNaoUtilDTO {

    private String Descricao;
    private Date dataInicial;
    private Date dataFinal;
}
