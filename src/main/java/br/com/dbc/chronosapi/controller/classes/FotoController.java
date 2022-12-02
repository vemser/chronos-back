package br.com.dbc.chronosapi.controller.classes;

import br.com.dbc.chronosapi.controller.interfaces.FotoControllerInterface;
import br.com.dbc.chronosapi.dto.usuario.UsuarioDTO;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.service.FotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/foto")

public class FotoController implements FotoControllerInterface {
    private final FotoService fotoService;


    @PutMapping(value = "/upload-image/{idUsuario}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<UsuarioDTO> uploadImage(@Valid @PathVariable("idUsuario") Integer idUsuario,
                                           @Valid @RequestPart("imagem") MultipartFile imagem) throws RegraDeNegocioException, IOException{
        return new ResponseEntity<>(fotoService.uploadImage(idUsuario, imagem), HttpStatus.OK);
    }

    @PutMapping(value = "/upload-image-perfil", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<UsuarioDTO> uploadImagePerfil(@Valid @RequestPart("imagem") MultipartFile imagem) throws RegraDeNegocioException, IOException {
        return new ResponseEntity<>(fotoService.uploadImagePerfil(imagem), HttpStatus.OK);
    }
}
