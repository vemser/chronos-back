package br.com.dbc.chronosapi.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CalendarioDTO {

    LocalDate dataInicial;
    LocalDate dataFinal;
}
