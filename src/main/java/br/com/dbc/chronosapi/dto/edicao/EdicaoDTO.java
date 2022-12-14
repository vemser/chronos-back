package br.com.dbc.chronosapi.dto.edicao;

import br.com.dbc.chronosapi.dto.etapa.EtapaDTO;
import br.com.dbc.chronosapi.entity.enums.Status;
import lombok.Data;

import java.util.List;

@Data
public class EdicaoDTO extends EdicaoCreateDTO{
    private Integer idEdicao;
    private Status status;
    private List<EtapaDTO> etapas;
}
