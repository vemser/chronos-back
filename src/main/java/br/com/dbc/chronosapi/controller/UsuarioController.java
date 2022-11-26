package br.com.dbc.chronosapi.controller;

import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.UsuarioCreateDTO;
import br.com.dbc.chronosapi.dto.UsuarioDTO;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<PageDTO<UsuarioDTO>> list(Integer pagina, Integer tamanho) throws RegraDeNegocioException {
        return ResponseEntity.ok(usuarioService.list(pagina, tamanho));
    }

//    @PostMapping("/registration")
//    public ResponseEntity<UsuarioDTO> registration(@Valid @RequestBody UsuarioCreateDTO usuario) throws RegraDeNegocioException {
//
//        log.info("Cadastro de usuário em andamento . . .");
//        UsuarioDTO usuarioDTO = usuarioService.registerUser(usuario);
//        log.info("Cadastro realizado.");
//
//        return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
//    }

    //    @PutMapping("/edit-register")
//    public ResponseEntity<UsuarioDTO> update(@Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException {
//        log.info("Atualizando usuário....");
//        UsuarioDTO usuarioDTO = usuarioService.edit(usuarioCreateDTO);
//        log.info("Usuário atualizado com sucesso!");
//
//        return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
//    }

//    @DeleteMapping("/{idUsuario}") // localhost:1521/pessoa/10
//    public ResponseEntity<UsuarioDTO> delete(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException {
//
//        log.info("Deletando a pessoa");
//        usuarioService.remover(id);
//        log.info("Deletado com sucesso!");
//
//        return ResponseEntity.noContent().build();
//    }

}
