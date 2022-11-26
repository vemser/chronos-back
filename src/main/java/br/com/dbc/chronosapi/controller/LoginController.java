package br.com.dbc.chronosapi.controller;


import br.com.dbc.chronosapi.dto.LoginDTO;
import br.com.dbc.chronosapi.security.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/login")
@Validated
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<String> login(@RequestBody @Valid LoginDTO loginDTO) {
        log.info("Verificando autenticação . . .");
        return new ResponseEntity<>(tokenService.authAccess(loginDTO, authenticationManager), HttpStatus.OK);
    }


//    @PostMapping("/forgot-password")
//    public ResponseEntity<String> sendRecoverPasswordMail(String email) throws RegraDeNegocioException {
//        return ResponseEntity.ok(usuarioService.sendRecoverMail(email));
//    }

//    @PostMapping("/change-password")
//    public ResponseEntity<String> updatePassword(String password) throws RegraDeNegocioException {
//        return ResponseEntity.ok(usuarioService.updatePassword(password));
//    }

}
