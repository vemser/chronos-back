package br.com.dbc.chronosapi.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class EdicaoCreateDTO {

    @NotNull
    @NotBlank
    private String nomeEdicao;

    private LocalDate dataInicio;

    private LocalDate dataTermino;
}
