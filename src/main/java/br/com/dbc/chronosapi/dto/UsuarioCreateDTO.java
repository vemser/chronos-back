package br.com.dbc.chronosapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UsuarioCreateDTO {

    @NotNull
    @Schema(description = "Nome do usuário", example = "Fulano")
    private String nome;

    @Email
    @Schema(description = "Email do usuário", example = "fulano@gmail.com")
    private String email;

    private String fotoFaseDeTeste;

    @NotNull
    private List<CargoDTO> cargos;
}
