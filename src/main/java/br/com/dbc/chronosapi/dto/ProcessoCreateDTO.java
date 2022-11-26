package br.com.dbc.chronosapi.dto;

import br.com.dbc.chronosapi.entity.EdicaoEntity;
import br.com.dbc.chronosapi.entity.EtapaEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class ProcessoCreateDTO {

    @NotNull
    private EtapaEntity etapa;

    @NotNull
    private AreaEnvolvidaEntity areaEnvolvida;

    @NotNull
    private ResponsavelEntity responsavel;

    @NotNull
    private Integer diasUteis;

    @NotNull
    @NotBlank
    private String ordemExecucao;

    private LocalDate duracaoProcesso;

    @NotNull
    private List<EdicaoEntity> edicoes;
}
