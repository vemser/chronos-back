package br.com.dbc.chronosapi.dto;

import br.com.dbc.chronosapi.entity.enums.Atividade;
import lombok.Data;

@Data
public class UsuarioDTO {

    private Integer idUsuario;
    private String nome;
    private String email;
    private Atividade ativo;
}
