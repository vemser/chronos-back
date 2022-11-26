package br.com.dbc.chronosapi.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    @NotNull
    @Email
    @Schema(example = "fulano@gmail.com")
    private String email;

    @NotNull
    @Schema(example = "123")
    private String senha;

}
