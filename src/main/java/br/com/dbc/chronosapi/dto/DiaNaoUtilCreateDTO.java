package br.com.dbc.chronosapi.dto;

import br.com.dbc.chronosapi.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class DiaNaoUtilCreateDTO {

    @NotNull
    @NotBlank
    @Schema(description = "descricao do dia não util")
    private String descricao;

    @NotNull
    @Schema(description = "data inicial do dia não util")
    private LocalDate dataInicial;

    @Schema(description = "data final do dia não util")
    private LocalDate dataFinal;

    @NotNull
    @Schema(description = "status da repetição anual do dia não util")
    private Status repeticaoAnual;

}
