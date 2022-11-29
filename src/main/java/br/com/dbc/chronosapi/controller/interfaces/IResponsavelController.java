package br.com.dbc.chronosapi.controller.interfaces;

import br.com.dbc.chronosapi.dto.processo.ResponsavelCreateDTO;
import br.com.dbc.chronosapi.dto.processo.ResponsavelDTO;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface IResponsavelController {

    @Operation(summary = "Criar um novo responsável", description = "Cria um novo responsável(campo de processo) no banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Responsavel criado com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    ResponseEntity<ResponsavelDTO> create(@Valid @RequestBody ResponsavelCreateDTO responsavelCreateDTO);

    @Operation(summary = "Deletar um responsável", description = "Deleta um responsavel(campo de processo) no banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Responsavel deletado com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    ResponseEntity<Void> delete (@Valid @PathVariable("idResponsavel") Integer idResponsavel) throws RegraDeNegocioException;


}
