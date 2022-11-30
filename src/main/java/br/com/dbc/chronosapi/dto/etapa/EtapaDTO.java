package br.com.dbc.chronosapi.dto.etapa;

import br.com.dbc.chronosapi.dto.processo.ProcessoDTO;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class EtapaDTO extends EtapaCreateDTO{
    private Integer idEtapa;
    private List<ProcessoDTO> processos;
}
