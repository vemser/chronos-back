package br.com.dbc.chronosapi.controller.classes;

import br.com.dbc.chronosapi.controller.interfaces.IUsuarioController;
import br.com.dbc.chronosapi.dto.PageDTO;
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

import java.io.IOException;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/usuario")
public class UsuarioController implements IUsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<PageDTO<UsuarioDTO>> list(Integer pagina, Integer tamanho) {
        return new ResponseEntity<>(usuarioService.list(pagina, tamanho), HttpStatus.OK);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<UsuarioDTO> create(@RequestParam String nome,
                                             @RequestParam String email,
                                             @RequestParam List<String> stringCargos,
                                             @RequestPart(name = "question-image", required = false) MultipartFile imagem) throws RegraDeNegocioException, IOException {
        log.info("Cadastrando usuário...");
        UsuarioDTO usuarioDTO = usuarioService.create(nome, email, stringCargos, imagem);
        log.info("Usuário cadastrado com sucesso!");

        return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
    }

    @PutMapping(value = "/update-perfil", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<UsuarioDTO> updatePerfil(@RequestBody UsuarioUpdateDTO usuarioUpdateDTO,
                                                   @RequestPart (name = "question-image", required = false) MultipartFile imagem) throws RegraDeNegocioException, IOException {
        log.info("Atualizando usuário....");
        UsuarioDTO usuarioDTO = usuarioService.updatePerfil(usuarioUpdateDTO, imagem);
        log.info("Usuário atualizado com sucesso!");

        return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
    }

    @PutMapping(value = "/update-cadastro/{idUsuario}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<UsuarioDTO> updateAdmin(@PathVariable("idUsuario") Integer idUsuario,
                                                     @RequestParam String nome,
                                                     @RequestParam List<String> stringCargos,
                                                     @RequestPart (name = "question-image", required = false) MultipartFile imagem) throws RegraDeNegocioException, IOException {
        log.info("Atualizando usuário....");
        UsuarioDTO usuarioDTO = usuarioService.updateAdmin(idUsuario, nome, stringCargos, imagem);
        log.info("Usuário atualizado com sucesso!");

        return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
    }

    @PutMapping("/enable-disable/{idUsuario}")
    public ResponseEntity<Void> enableOrDisable(@PathVariable("idUsuario") Integer idUsuario) throws RegraDeNegocioException {
        usuarioService.enableOrDisable(idUsuario);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> delete(@PathVariable("idUsuario") Integer idUsuario) throws RegraDeNegocioException {
        log.info("Deletando usuário...");
        usuarioService.delete(idUsuario);
        log.info("Usuário deletado com sucesso!");

        return ResponseEntity.noContent().build();
    }
}
