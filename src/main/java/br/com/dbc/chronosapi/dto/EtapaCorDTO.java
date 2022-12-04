package br.com.dbc.chronosapi.dto;

import br.com.dbc.chronosapi.entity.classes.EdicaoEntity;
import br.com.dbc.chronosapi.entity.classes.processos.ProcessoEntity;
import lombok.Data;

import java.util.Set;

@Data
public class EtapaCorDTO {
    private Integer idEtapa;
    private Integer idEdicao;
    private String nome;
    private Integer ordemExecucao;
    private String cor;
    private EdicaoEntity edicao;
    private Set<ProcessoEntity> processos;
}
