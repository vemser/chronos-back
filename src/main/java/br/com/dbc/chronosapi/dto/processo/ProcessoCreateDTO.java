package br.com.dbc.chronosapi.dto.processo;

import br.com.dbc.chronosapi.entity.classes.EdicaoEntity;
import br.com.dbc.chronosapi.entity.classes.EtapaEntity;
import br.com.dbc.chronosapi.entity.classes.processos.AreaEnvolvidaEntity;
import br.com.dbc.chronosapi.entity.classes.processos.ResponsavelEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ProcessoCreateDTO {

    @NotNull
    @NotBlank
    private String nome;

    @NotNull
    private EtapaEntity etapa;

    @NotNull
    private AreaEnvolvidaEntity areaEnvolvida;

    @NotNull
    private ResponsavelEntity responsavel;

    @NotNull
    private Integer diasUteis;

    @NotNull
    private Integer ordemExecucao;

    @NotNull
    private String duracaoProcesso;

    @NotNull
    private List<EdicaoEntity> edicoes;
}
