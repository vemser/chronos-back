package br.com.dbc.chronosapi.dto;

import br.com.dbc.chronosapi.dto.etapa.EtapaDTO;
import br.com.dbc.chronosapi.dto.processo.ProcessoDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DiaDTO {
    private LocalDate dia;
    private boolean diaUtil;
    private boolean diaNaoUtil;
    private EtapaDTO etapa;
    private ProcessoDTO processo;
}
