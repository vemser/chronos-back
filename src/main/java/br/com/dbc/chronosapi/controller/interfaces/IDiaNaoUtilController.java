package br.com.dbc.chronosapi.controller.interfaces;

import br.com.dbc.chronosapi.dto.diaNaoUtil.DiaNaoUtilCreateDTO;
import br.com.dbc.chronosapi.dto.diaNaoUtil.DiaNaoUtilDTO;
import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.entity.enums.Status;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

public interface IDiaNaoUtilController {

    @Operation(summary = "Listar todos os dias não úteis", description = "Lista todos os dias não úteis presentes no banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    ResponseEntity<PageDTO<DiaNaoUtilDTO>> list(Integer pagina, Integer tamanho);

    @Operation(summary = "Criar um novo dia não util", description = "Cria um novo dia não util no banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Dia não util criado com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    ResponseEntity<DiaNaoUtilDTO> create(@Valid @RequestBody DiaNaoUtilCreateDTO diaNaoUtilCreateDTO,
                                         @Valid @RequestParam Status repeticaoAnual);

    @Operation(summary = "Atualizar dia não util", description = "Atualiza um dia não util no banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Dia não util atualizado com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    ResponseEntity<DiaNaoUtilDTO> update(@Valid @PathVariable ("idDiaNaoUtil") Integer idDiaNaoUtil,
                                         @Valid @RequestBody DiaNaoUtilCreateDTO diaNaoUtilCreateDTO,
                                         @Valid @RequestParam Status repeticaoAnual) throws RegraDeNegocioException;

    @Operation(summary = "Deletar um dia não util", description = "Deleta um dia não util do banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Dia não util deletado com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    ResponseEntity<Void> delete(@Valid @PathVariable("idDiaNaoUtil") Integer idDiaNaoUtil) throws RegraDeNegocioException;
}
