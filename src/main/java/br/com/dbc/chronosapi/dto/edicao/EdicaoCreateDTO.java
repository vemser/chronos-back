package br.com.dbc.chronosapi.dto.edicao;

import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class EdicaoCreateDTO {

    @NotNull
    @NotBlank
    private String nome;

    @FutureOrPresent
    private LocalDate dataInicial;

    @Future
    private LocalDate dataFinal;
}
