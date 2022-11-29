package br.com.dbc.chronosapi.dto.usuario;

import lombok.Data;

@Data
public class CargoDTO {
    private Integer idCargo;
    private String nome;
    private String descricao;
}
