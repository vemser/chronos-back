package br.com.dbc.chronosapi.controller.classes;

import br.com.dbc.chronosapi.controller.interfaces.FotoControllerInterface;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.service.FotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/foto")

public class FotoController implements FotoControllerInterface {
    private final FotoService fotoService;

    @PutMapping(value = "/upload-foto", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> uploadFoto(@RequestPart("file") MultipartFile file,
                                           @RequestParam("email") String email) throws RegraDeNegocioException, IOException {
        fotoService.arquivarUsuario(file, email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recuperar-foto")
    public ResponseEntity<String> fotoRecover(@RequestParam("email") String email) throws RegraDeNegocioException{
        return new ResponseEntity<>(fotoService.pegarImagemUsuario(email), HttpStatus.OK);
    }
}
