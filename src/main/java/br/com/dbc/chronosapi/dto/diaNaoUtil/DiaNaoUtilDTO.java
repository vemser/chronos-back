package br.com.dbc.chronosapi.dto.diaNaoUtil;

import br.com.dbc.chronosapi.entity.enums.Status;
import lombok.Data;

@Data
public class DiaNaoUtilDTO extends DiaNaoUtilCreateDTO {

    private Integer idDiaNaoUtil;

    private Status repeticaoAnual;
}
