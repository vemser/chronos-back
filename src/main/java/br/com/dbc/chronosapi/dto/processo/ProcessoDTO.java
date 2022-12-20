package br.com.dbc.chronosapi.dto.processo;


import br.com.dbc.chronosapi.entity.enums.Status;
import lombok.Data;

import java.util.Set;

@Data
public class ProcessoDTO {
    private Integer idProcesso;
    private String nome;
    private Set<AreaEnvolvidaDTO> areasEnvolvidas;
    private Set<ResponsavelDTO> responsaveis;
    private String duracaoProcesso;
    private Integer diasUteis;
    private Integer ordemExecucao;
    private Status processoCritico;
}
