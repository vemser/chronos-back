package br.com.dbc.chronosapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class DadosCalendarioDTO {
    private List<DiaDTO> dias;
    private String descricao;
    private String previsaoEncerramento;
}
