package br.com.dbc.chronosapi.controller.interfaces;

import br.com.dbc.chronosapi.dto.edicao.EdicaoCreateDTO;
import br.com.dbc.chronosapi.dto.edicao.EdicaoDTO;
import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface IEdicaoController {

    @Operation(summary = "Criar uma nova edição", description = "Cria uma nova edição do banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Edição criada com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping()
     ResponseEntity<EdicaoDTO> create(@Valid @RequestBody EdicaoCreateDTO edicaoCreateDTO);

    @Operation(summary = "Atualizar edição", description = "Atualiza uma edição do banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Edição atualizada com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PutMapping("/{id-edicao}")
     ResponseEntity<EdicaoDTO> update(@Valid @PathVariable("id-edicao") Integer idEdicao,
                                            @Valid @RequestBody EdicaoCreateDTO edicaoCreateDTO) throws RegraDeNegocioException;

    @Operation(summary = "Deletar uma edição", description = "Deleta uma edição do banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Edição deletada com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @DeleteMapping("/{id-edicao}")
     ResponseEntity<Void> delete(@Valid @PathVariable ("id-edicao") Integer idEdicao) throws RegraDeNegocioException;

    @Operation(summary = "Listar todas as edições", description = "Lista todos as edições presentes no banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping
    ResponseEntity<PageDTO<EdicaoDTO>> list(Integer pagina, Integer tamanho) throws RegraDeNegocioException;

    @Operation(summary = "Alterar status de uma edição", description = "Desabilita uma edição se ela estiver habilitada e caso ela esteja desabilitada ocorre o inverso.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Status de edição alterada com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PutMapping("/enable-disable/{idEdicao}")
    ResponseEntity<Void> enableOrDisable(@Valid @PathVariable("idEdicao") Integer idEdicao) throws RegraDeNegocioException;
}
