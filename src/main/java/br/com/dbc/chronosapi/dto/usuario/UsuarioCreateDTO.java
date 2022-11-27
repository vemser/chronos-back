package br.com.dbc.chronosapi.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UsuarioCreateDTO {

    @NotNull
    @NotBlank
    @Schema(description = "Nome do usuário", example = "Fulano")
    private String nome;

    @Email
    @Schema(description = "Email do usuário", example = "fulano@gmail.com")
    private String email;

    @NotNull
    @Schema(description = "Imagem do usuário")
    private byte[] imagem;

    @NotNull
    @Schema(description = "Cargos do usuário")
    private List<String> cargos;
}
