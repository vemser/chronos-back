package br.com.dbc.chronosapi.dto;

import br.com.dbc.chronosapi.dto.etapa.EtapaDTO;
import br.com.dbc.chronosapi.dto.processo.ProcessoDTO;
import lombok.Data;

@Data
public class JuncaoEdicoesDTO {
    private String edicao;
    private EtapaDTO etapa;
    private ProcessoDTO processo;
}
