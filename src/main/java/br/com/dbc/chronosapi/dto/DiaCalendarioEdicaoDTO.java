package br.com.dbc.chronosapi.dto;

import br.com.dbc.chronosapi.dto.etapa.EtapaDTO;
import br.com.dbc.chronosapi.dto.processo.ProcessoDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DiaCalendarioEdicaoDTO {
    private LocalDate dia;
    private DiaUtilDTO diaUtil;
    private EtapaDTO etapa;
    private ProcessoDTO processo;
}
