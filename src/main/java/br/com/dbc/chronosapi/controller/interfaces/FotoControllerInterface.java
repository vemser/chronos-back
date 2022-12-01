package br.com.dbc.chronosapi.controller.interfaces;

import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FotoControllerInterface {
    @Operation(summary = "Inserir foto ao usuario", description = "Insere uma foto ao usuario presente no banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Foto inserida com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    ResponseEntity<Void> uploadFoto(@RequestPart("file") MultipartFile file,
                                    @RequestParam("email") String email) throws RegraDeNegocioException, IOException;

    @Operation(summary = "Recuperar uma foto no sistema", description = "Recupera a foto de um usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foto recuperada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Erro na inserção de dados."),
            @ApiResponse(responseCode = "403", description = "Foi gerada uma exceção.")
    })
    ResponseEntity<String> fotoRecover(@RequestParam("email") String email) throws RegraDeNegocioException;
}
