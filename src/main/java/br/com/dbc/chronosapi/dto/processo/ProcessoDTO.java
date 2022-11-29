package br.com.dbc.chronosapi.dto.processo;

import lombok.Data;

import java.util.Set;

@Data
public class ProcessoDTO {
    private Integer idProcesso;
    private String nome;
    private Set<AreaEnvolvidaDTO> areaEnvolvida;
    private Set<ResponsavelDTO> responsavel;
    private String duracaoProcesso;
    private Integer diasUteis;
    private Integer ordemExecucao;
}
