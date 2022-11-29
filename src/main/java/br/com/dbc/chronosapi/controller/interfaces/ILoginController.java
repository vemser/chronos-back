package br.com.dbc.chronosapi.controller.interfaces;

import br.com.dbc.chronosapi.dto.usuario.LoginDTO;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface ILoginController {

    @Operation(summary = "Autenticar Login", description = "Autentica um usuário presente no banco de dados enviando-lhe o token de acesso.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Token enviado com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping
     ResponseEntity<String> login(@RequestBody @Valid LoginDTO loginDTO);

    @Operation(summary = "Esquecer senha", description = "Envia um email para redefinição da senha presente no banco de dados.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Email enviado com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping("/forgot-password")
     ResponseEntity<String> sendRecoverPasswordEmail(String email) throws RegraDeNegocioException;

    @Operation(summary = "Trocar senha", description = "EndPoint para a atualização da senha presente no banco de dados.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Senha atualizada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping("/change-password")
     ResponseEntity<String> updatePassword(String password) throws RegraDeNegocioException;

}
