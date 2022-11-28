package br.com.dbc.chronosapi.dto;

import br.com.dbc.chronosapi.entity.enums.Status;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDate;

@Data
public class DiaNaoUtilCreateDTO {

    private String descricao;

    private LocalDate dataInicial;

    private LocalDate dataFinal;

    private Status repeticaoAnual;

}
