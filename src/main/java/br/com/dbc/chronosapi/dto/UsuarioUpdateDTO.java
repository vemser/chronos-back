package br.com.dbc.chronosapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UsuarioUpdateDTO {

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
    private String corfirmacaoNovaSenha;
}
