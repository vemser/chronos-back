package br.com.dbc.chronosapi.controller.interfaces;

import br.com.dbc.chronosapi.dto.processo.AreaEnvolvidaCreateDTO;
import br.com.dbc.chronosapi.dto.processo.AreaEnvolvidaDTO;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface IAreaEnvolvidaController {

    @Operation(summary = "Criar uma nova área envolvida", description = "Cria uma nova área no banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Área envolvida criada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    ResponseEntity<AreaEnvolvidaDTO> create(@Valid @RequestBody AreaEnvolvidaCreateDTO areaEnvolvidaCreateDTO);

    @Operation(summary = "Deletar uma área envolvida", description = "Deleta uma area envolvida(campo de processo) no banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Área envolvida deletada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    ResponseEntity<Void> delete (@Valid @PathVariable("idAreaEnvolvida") Integer idAreaEnvolvida) throws RegraDeNegocioException;
}
