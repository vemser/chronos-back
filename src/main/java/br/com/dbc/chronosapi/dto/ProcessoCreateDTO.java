package br.com.dbc.chronosapi.dto;

import br.com.dbc.chronosapi.entity.classes.EdicaoEntity;
import br.com.dbc.chronosapi.entity.classes.EtapaEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ProcessoCreateDTO {

    @NotNull
    private EtapaEntity etapa;

    @NotNull
    @NotBlank
    private String areaEnvolvida;

    @NotNull
    @NotBlank
    private String responsavel;

    @NotNull
    private Integer diasUteis;

    @NotNull
    private Integer ordemExecucao;

    @NotNull
    private Integer duracaoProcesso;

    @NotNull
    private List<EdicaoEntity> edicoes;
}
