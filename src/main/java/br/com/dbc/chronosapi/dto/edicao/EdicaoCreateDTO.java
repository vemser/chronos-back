package br.com.dbc.chronosapi.dto.edicao;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class EdicaoCreateDTO {

    @NotNull
    @NotBlank
    private String nome;

    @NotNull
    private LocalDate dataInicial;

    @NotNull
    private LocalDate dataFinal;
}
