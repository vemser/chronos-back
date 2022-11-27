package br.com.dbc.chronosapi.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UsuarioDefaultCreateDTO {
    @NotNull
    @Schema(description = "Nome do usuário", example = "Fulano")
    private String nome;

    @NotNull
    @NotBlank
    @Schema(description = "Senha atual do usuário", example = "1234")
    private String senhaAtual;

    @NotNull
    @NotBlank
    @Schema(description = "Nova Senha do usuário", example = "123456")
    private String novaSenha;

    @NotNull
    @NotBlank
    @Schema(description = "Confitmação da nova senha do usuário", example = "123456")
    private String confirmacaoNovaSenha;
}
