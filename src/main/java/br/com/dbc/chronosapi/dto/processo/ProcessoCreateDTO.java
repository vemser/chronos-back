package br.com.dbc.chronosapi.dto.processo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class ProcessoCreateDTO {

    @NotNull
    @NotBlank
    private String nome;

    @NotNull
    private Set<AreaEnvolvidaCreateDTO> areasEnvolvidas;

    @NotNull
    private Set<ResponsavelCreateDTO> responsaveis;

    @NotNull
    @NotBlank
    private String duracaoProcesso;

    @NotNull
    private Integer diasUteis;

    @NotNull
    private Integer ordemExecucao;
}
