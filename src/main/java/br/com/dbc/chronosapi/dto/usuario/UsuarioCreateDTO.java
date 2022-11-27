package br.com.dbc.chronosapi.dto.usuario;

import br.com.dbc.chronosapi.dto.CargoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Lob;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class UsuarioCreateDTO {

    @NotNull
    @NotBlank
    @Schema(description = "Nome do usuário", example = "Fulano")
    private String nome;

    @Email
    @Schema(description = "Email do usuário", example = "fulano@gmail.com")
    private String email;

    @Lob
    @NotNull
    @Schema(description = "Imagem do usuário")
    private byte[] imagem;

    @NotNull
    @Schema(description = "Cargos do usuário")
    private Set<CargoDTO> cargos;
}
