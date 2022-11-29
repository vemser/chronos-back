package br.com.dbc.chronosapi.dto.usuario;

import lombok.Data;

import java.util.Set;

@Data
public class UsuarioDTO {
    private Integer IdUsuario;
    private String nome;
    private String email;
    private Set<CargoDTO> cargos;
    private byte[] imagem;
}
