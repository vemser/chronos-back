package br.com.dbc.chronosapi.controller.classes;

import br.com.dbc.chronosapi.controller.interfaces.UsuarioControllerInterface;
import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.usuario.UAdminUpdateDTO;
import br.com.dbc.chronosapi.dto.usuario.UsuarioCreateDTO;
import br.com.dbc.chronosapi.dto.usuario.UsuarioDTO;
import br.com.dbc.chronosapi.dto.usuario.UsuarioUpdateDTO;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/usuario")
public class UsuarioController implements UsuarioControllerInterface {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<PageDTO<UsuarioDTO>> list(Integer pagina, Integer tamanho) {
        return new ResponseEntity<>(usuarioService.list(pagina, tamanho), HttpStatus.OK);
    }

    @GetMapping("/logged-user")
    public ResponseEntity <UsuarioDTO> getLoggedUser() throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.buscarUsuarioLogado(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> create(@Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException, IOException {
        log.info("Cadastrando usuário...");
        UsuarioDTO usuarioDTO = usuarioService.create(usuarioCreateDTO);
        log.info("Usuário cadastrado com sucesso!");

        return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
    }

    @PutMapping(value = "/upload-image/{idUsuario}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<UsuarioDTO> uploadImage(@Valid @PathVariable("idUsuario") Integer idUsuario,
                                                  @Valid @RequestPart("file") MultipartFile imagem) throws RegraDeNegocioException, IOException {
        return new ResponseEntity<>(usuarioService.uploadImage(idUsuario, imagem), HttpStatus.OK);
    }

    @PutMapping(value = "/upload-image-perfil", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<UsuarioDTO> uploadImagePerfil(@Valid @RequestPart("file") MultipartFile imagem) throws RegraDeNegocioException, IOException {
        return new ResponseEntity<>(usuarioService.uploadImagePerfil(imagem), HttpStatus.OK);
    }

    @PutMapping(value = "/update-perfil")
    public ResponseEntity<UsuarioDTO> updatePerfil(@Valid @RequestBody UsuarioUpdateDTO usuarioUpdateDTO) throws RegraDeNegocioException, IOException {
        log.info("Atualizando usuário....");
        UsuarioDTO usuarioDTO = usuarioService.updatePerfil(usuarioUpdateDTO);
        log.info("Usuário atualizado com sucesso!");

        return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
    }

    @PutMapping(value = "/update-cadastro/{idUsuario}")
    public ResponseEntity<UsuarioDTO> updateAdmin(@Valid @PathVariable("idUsuario") Integer idUsuario,
                                                  @Valid @RequestBody UAdminUpdateDTO uAdminUpdateDTO) throws RegraDeNegocioException, IOException {
        log.info("Atualizando usuário....");
        UsuarioDTO usuarioDTO = usuarioService.updateAdmin(idUsuario, uAdminUpdateDTO);
        log.info("Usuário atualizado com sucesso!");

        return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
    }

    @PutMapping("/enable-disable/{idUsuario}")
    public ResponseEntity<Void> enableOrDisable(@Valid @PathVariable("idUsuario") Integer idUsuario) throws RegraDeNegocioException {
        usuarioService.enableOrDisable(idUsuario);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> delete(@Valid @PathVariable("idUsuario") Integer idUsuario) throws RegraDeNegocioException {
        log.info("Deletando usuário...");
        usuarioService.delete(idUsuario);
        log.info("Usuário deletado com sucesso!");

        return ResponseEntity.noContent().build();
    }
}
