package br.com.dbc.chronosapi.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CargoDTO {

    @NotNull
    private Integer idCargo;

    @NotNull
    private String nome;
}
