package br.com.dbc.chronosapi.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
@Data
public class UsuarioUpdateDTO {
    @NotNull
    @NotBlank
    @Schema(description = "Nome do usuário")
    private String nome;
    @NotNull
    @NotBlank
    @Schema(description = "Senha atual do usuário")
    private String senhaAtual;
    @NotNull
    @NotBlank
    @Schema(description = "Nova senha do usuário")
    private String novaSenha;
    @NotNull
    @NotBlank
    @Schema(description = "Confirmação da nova senha do usuário")
    private String confirmacaoNovaSenha;
}
