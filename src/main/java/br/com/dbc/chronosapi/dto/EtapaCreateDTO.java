package br.com.dbc.chronosapi.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EtapaCreateDTO {

    @NotNull
    @NotBlank
    private String nomeEtapa;
}
