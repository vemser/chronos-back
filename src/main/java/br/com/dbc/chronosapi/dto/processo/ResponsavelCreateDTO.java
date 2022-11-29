package br.com.dbc.chronosapi.dto.processo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ResponsavelCreateDTO {

    @NotNull
    @NotBlank
    private String nome;
}
