package br.com.dbc.chronosapi.controller.interfaces;

import br.com.dbc.chronosapi.dto.EtapaCreateDTO;
import br.com.dbc.chronosapi.dto.EtapaDTO;
import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface IEtapaController {

    @Operation(summary = "Criar uma nova etapa", description = "Cria uma nova etapa no banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Etapa criada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping
    ResponseEntity<EtapaDTO> create(@Valid @RequestParam EtapaCreateDTO etapaCreateDTO);

    @Operation(summary = "Atualizar etapa", description = "Atualiza uma etapa do banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Etapa atualizada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PutMapping("/{id-etapa}")
    ResponseEntity<EtapaDTO> update(@Valid @PathVariable("id-etapa") Integer idEtapa,
                                    @Valid @RequestBody EtapaCreateDTO etapaCreateDTO) throws RegraDeNegocioException;

    @Operation(summary = "Deletar uma etapa", description = "Deleta uma etapa no banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Etapa deletada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @DeleteMapping("/{id-etapa}")
    ResponseEntity<Void> delete(@Valid @PathVariable("id-etapa") Integer idEtapa) throws RegraDeNegocioException;

    @Operation(summary = "Listar todas as etapas", description = "Lista todos as etapas presentes no banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista de etapas"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/listar-etapas")
    ResponseEntity<PageDTO<EtapaDTO>> list(Integer pagina, Integer tamanho);
}

