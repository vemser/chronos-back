package br.com.dbc.chronosapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UsuarioDTO extends UsuarioCreateDTO {

    @NotNull
    @Schema(description = "Id do usuário", example = "1")
    private Integer IdUsuario;

}
