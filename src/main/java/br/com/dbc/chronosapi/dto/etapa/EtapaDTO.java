package br.com.dbc.chronosapi.dto.etapa;

import br.com.dbc.chronosapi.dto.processo.ProcessoDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Set;

@Data
public class EtapaDTO extends EtapaCreateDTO{
    private Integer idEtapa;
    @JsonIgnore
    private Set<ProcessoDTO> processos;
}
