package br.com.dbc.chronosapi.controller.classes;


import br.com.dbc.chronosapi.controller.interfaces.ILoginController;
import br.com.dbc.chronosapi.dto.LoginDTO;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.security.TokenService;
import br.com.dbc.chronosapi.service.LoginService;
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
public class LoginController implements ILoginController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<String> login(@RequestBody @Valid LoginDTO loginDTO) {
        log.info("Verificando autenticação . . .");
        return new ResponseEntity<>(tokenService.authAccess(loginDTO, authenticationManager), HttpStatus.OK);

    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> sendRecoverPasswordEmail(String email) throws RegraDeNegocioException {
        return ResponseEntity.ok(loginService.sendRecoverPasswordEmail(email));
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> updatePassword(String password) throws RegraDeNegocioException {
        return ResponseEntity.ok(loginService.updatePassword(password));
    }


}
