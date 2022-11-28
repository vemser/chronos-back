package br.com.dbc.chronosapi.dto.usuario;

import br.com.dbc.chronosapi.dto.CargoDTO;
import lombok.Data;

import java.util.Set;

@Data
public class UsuarioDTO extends UsuarioCreateDTO {
    private Integer IdUsuario;
    private Set<CargoDTO> cargos;
    private byte[] imagem;
}
