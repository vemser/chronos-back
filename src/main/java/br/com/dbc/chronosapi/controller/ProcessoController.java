package br.com.dbc.chronosapi.controller;

import br.com.dbc.chronosapi.dto.ProcessoCreateDTO;
import br.com.dbc.chronosapi.dto.ProcessoDTO;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.service.ProcessoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/processo")
public class ProcessoController {

    private final ProcessoService processoService;

    @Operation(summary = "Listar todos os processos", description = "Lista todos os processos do banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista de processos"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping()
    public ResponseEntity<List<ProcessoDTO>> list() {
        return new ResponseEntity<>(processoService.list(), HttpStatus.OK);
    }

    @Operation(summary = "Criar um novo processo", description = "Cria um novo processo do banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Processo criado com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping()
    public ResponseEntity<ProcessoDTO> create(@RequestBody ProcessoCreateDTO processoCreateDTO) {
        return new ResponseEntity<>(processoService.create(processoCreateDTO), HttpStatus.OK);
    }

    @Operation(summary = "Atualizar processo", description = "Atualiza um processo do banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Processo atualizado com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PutMapping("/{idProcesso}")
    public ResponseEntity<ProcessoDTO> update(@PathVariable("idProcesso") Integer idProcesso,
                                              @RequestBody ProcessoCreateDTO processoUpdate) throws RegraDeNegocioException {
        return new ResponseEntity<>(processoService.update(idProcesso, processoUpdate), HttpStatus.OK);
    }

    @Operation(summary = "Deletar processo", description = "Deleta um processo do banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Processo deletado com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @DeleteMapping("/{idProcesso}")
    public ResponseEntity<Void> delete(@PathVariable("idProcesso") Integer idProcesso) throws RegraDeNegocioException {
        processoService.delete(idProcesso);
        return ResponseEntity.noContent().build();
    }
}
