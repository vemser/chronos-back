package br.com.dbc.chronosapi.dto;

import lombok.Data;

@Data
public class DiaUtilDTO {
    private boolean ehDiaUtil;
    private boolean ehDiaNaoUtil;
    private String descricao;
}
