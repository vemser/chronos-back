package br.com.dbc.chronosapi.controller.interfaces;

import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.usuario.UAdminUpdateDTO;
import br.com.dbc.chronosapi.dto.usuario.UsuarioCreateDTO;
import br.com.dbc.chronosapi.dto.usuario.UsuarioDTO;
import br.com.dbc.chronosapi.dto.usuario.UsuarioUpdateDTO;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

public interface IUsuarioController {

    @Operation(summary = "Listar usuários", description = "Lista todos os usuários presentes no banco de dados.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Usuarios listados com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping
    ResponseEntity<PageDTO<UsuarioDTO>> list(Integer pagina, Integer tamanho);

    @Operation(summary = "Criar usuário", description = "Cria um novo usuário no banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Usuário criado com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<UsuarioDTO> create(@RequestBody UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException, IOException ;

    @Operation(summary = "Inserir foto ao usuario", description = "Insere uma foto ao usuario presente no banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Foto inserida com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PutMapping("/uploadImage/{idUsuario}")
    ResponseEntity<UsuarioDTO> uploadImage(@Valid @PathVariable("idUsuario") Integer idUsuario,
                                                  @Valid @RequestPart (name = "question-image", required = false) MultipartFile imagem) throws RegraDeNegocioException, IOException;

    @Operation(summary = "Atualizar perfil do usuário", description = "Endpoint para o usuário poder atualizar o próprio perfil presente no banco de dados.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PutMapping(value = "/update-perfil", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<UsuarioDTO> updatePerfil(@RequestBody UsuarioUpdateDTO usuarioUpdateDTO) throws RegraDeNegocioException, IOException;

    @Operation(summary = "Atualizar cadastro e cargo do usuário", description = "Endpoint para o admin poder atualizar informações do usuário possibilitando atualizar o cargo do mesmo.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PutMapping(value = "/update-cadastro/{idUsuario}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<UsuarioDTO> updateAdmin(@PathVariable("idUsuario") Integer idUsuario,
                                           @RequestBody UAdminUpdateDTO uAdminUpdateDTO) throws RegraDeNegocioException, IOException;

    @Operation(summary = "Deletar usuário", description = "Deleta um usuário presente no banco de dados através do seu id.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @DeleteMapping("/{idUsuario}")
    ResponseEntity<Void> delete(@PathVariable("idUsuario") Integer idUsuario) throws RegraDeNegocioException;

    @Operation(summary = "Alterar status do usuário", description = "Habilita o usuário se ele estiver desabilitado e desabilita se ele estiver habilitado.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Status do usuário alterado com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PutMapping("/enable-disable/{idUsuario}")
    ResponseEntity<Void> enableOrDisable(@PathVariable("idUsuario") Integer idUsuario) throws RegraDeNegocioException;
}
