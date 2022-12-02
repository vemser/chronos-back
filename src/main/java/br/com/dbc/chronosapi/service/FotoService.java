package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.dto.usuario.UsuarioDTO;
import br.com.dbc.chronosapi.entity.classes.FotoEntity;
import br.com.dbc.chronosapi.entity.classes.UsuarioEntity;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.FotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FotoService {

    private final UsuarioService usuarioService;
    private final FotoRepository fotoRepository;
    private final LoginService loginService;

    public FotoEntity findById(Integer idPerfil) throws RegraDeNegocioException {
        return fotoRepository.findById(idPerfil)
                .orElseThrow(() -> new RegraDeNegocioException("Imagem não encontrada!"));
    }

    public UsuarioDTO uploadImage(Integer idUsuario, MultipartFile imagem) throws RegraDeNegocioException, IOException {
        UsuarioEntity usuario = usuarioService.findById(idUsuario);
        return SalvarUsuarioComFotoDTO(imagem, usuario);
    }

    public UsuarioDTO uploadImagePerfil(MultipartFile imagem) throws RegraDeNegocioException, IOException {
        Integer idLoggedUser = loginService.getIdLoggedUser();

        UsuarioEntity usuario = usuarioService.findById(idLoggedUser);
        return SalvarUsuarioComFotoDTO(imagem, usuario);
    }

    public UsuarioDTO SalvarUsuarioComFotoDTO(MultipartFile imagem, UsuarioEntity usuario) throws IOException {
        FotoEntity fotoRecuperada = fotoRepository.findByUsuario(usuario);
        UsuarioDTO usuarioDTO;
        if(fotoRecuperada == null) {
            FotoEntity fotoEntity = new FotoEntity();
            usuarioDTO = getUsuarioDTO(imagem, usuario, fotoEntity);

        }else {
             usuarioDTO = getUsuarioDTO(imagem, usuario, fotoRecuperada);
        }
        return usuarioDTO;
    }

    private UsuarioDTO getUsuarioDTO(MultipartFile imagem, UsuarioEntity usuario, FotoEntity fotoEntity) throws IOException {
        UsuarioDTO usuarioDTO;
        String nomeFoto = StringUtils.cleanPath((imagem.getOriginalFilename()));
        fotoEntity.setArquivo(imagem.getBytes());
        fotoEntity.setTipo(imagem.getContentType());
        fotoEntity.setNome(nomeFoto);
        fotoEntity.setUsuario(usuario);
        usuario.setFoto(fotoEntity);
        FotoEntity fotoSaved = fotoRepository.save(fotoEntity);
        usuarioDTO = usuarioService.salvarUsuario(usuario);
        usuarioDTO.setCargos(usuarioService.getCargosDTO(usuario.getCargos()));
        usuarioDTO.setImagem(fotoSaved.getArquivo());
        return usuarioDTO;
    }
}
