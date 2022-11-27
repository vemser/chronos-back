package br.com.dbc.chronosapi.dto.usuario;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UsuarioDTO extends UsuarioCreateDTO {
    @NotNull
    private Integer IdUsuario;
}
