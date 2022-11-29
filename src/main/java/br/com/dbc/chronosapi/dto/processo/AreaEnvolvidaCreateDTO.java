package br.com.dbc.chronosapi.dto.processo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AreaEnvolvidaCreateDTO {

    @NotNull
    @NotBlank
    private String nome;
}
