package br.com.dbc.chronosapi.dto.usuario;

import br.com.dbc.chronosapi.entity.enums.Status;
import lombok.Data;

import java.util.Set;

@Data
public class UsuarioDTO {
    private Integer IdUsuario;
    private String nome;
    private String email;
    private Status status;
    private Set<CargoDTO> cargos;
    private byte[] imagem;
}
