package br.com.dbc.chronosapi.controller.interfaces;

import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.processo.ProcessoCreateDTO;
import br.com.dbc.chronosapi.dto.processo.ProcessoDTO;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface ProcessoControllerInterface {

    @Operation(summary = "Listar todos os processos", description = "Lista todos os processos do banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista de processos"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping()
    ResponseEntity<PageDTO<ProcessoDTO>> list(Integer pagina, Integer tamanho) throws RegraDeNegocioException;

    @Operation(summary = "Listar todos os processos de uma etapa", description = "Lista todos os processos atrelados a uma etapa presente no banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    ResponseEntity<List<ProcessoDTO>> listProcessosPorEtapa(@PathVariable("idEtapa") Integer idEtapa) throws RegraDeNegocioException;

    @Operation(summary = "Criar um novo processo", description = "Cria um novo processo do banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Processo criado com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping("/{idEtapa}")
    ResponseEntity<ProcessoDTO> create(@PathVariable("idEtapa") Integer idEtapa,
                                       @Valid @RequestBody ProcessoCreateDTO processoCreateDTO) throws RegraDeNegocioException;

    @Operation(summary = "Atualizar processo", description = "Atualiza um processo do banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Processo atualizado com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PutMapping("/{idProcesso}")
    ResponseEntity<ProcessoDTO> update(@PathVariable("idProcesso") Integer idProcesso,
                                       @Valid @RequestBody ProcessoCreateDTO processoUpdate) throws RegraDeNegocioException;

    @Operation(summary = "Deletar processo", description = "Deleta um processo do banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Processo deletado com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @DeleteMapping("/{idProcesso}")
    ResponseEntity<Void> delete(@PathVariable("idProcesso") Integer idProcesso) throws RegraDeNegocioException;

}
