package br.com.dbc.chronosapi.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.util.List;

public class UsuarioDefaultDTO {

    @NotNull
    @Schema(description = "Id do usuário")
    private Integer IdUsuario;

    @NotNull
    @Schema(description = "Cargos do usuário")
    private List<String> cargos;
}
