package br.com.dbc.chronosapi.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Data
public class EdicaoCreateDTO {

    @NotNull
    @NotBlank
    private String nomeEdicao;

    private LocalDate dataInicial;

    private LocalDate dataFinal;

}
