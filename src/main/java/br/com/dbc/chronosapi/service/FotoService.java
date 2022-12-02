package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.dto.usuario.UsuarioDTO;
import br.com.dbc.chronosapi.entity.classes.CargoEntity;
import br.com.dbc.chronosapi.entity.classes.FotoEntity;
import br.com.dbc.chronosapi.entity.classes.UsuarioEntity;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.FotoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FotoService {

    private final UsuarioService usuarioService;
    private final FotoRepository fotoRepository;
    private final LoginService loginService;
    private final ObjectMapper objectMapper;

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

    private UsuarioDTO SalvarUsuarioComFotoDTO(MultipartFile imagem, UsuarioEntity usuario) throws IOException {
        FotoEntity fotoRecuperada = fotoRepository.findByUsuario(usuario);
        if(fotoRecuperada == null) {
            FotoEntity fotoEntity = new FotoEntity();
            String nomeFoto = StringUtils.cleanPath((imagem.getOriginalFilename()));
            fotoEntity.setArquivo(imagem.getBytes());
            fotoEntity.setTipo(imagem.getContentType());
            fotoEntity.setNome(nomeFoto);
            fotoEntity.setUsuario(usuario);
        }else {
            String nomeFoto = StringUtils.cleanPath((imagem.getOriginalFilename()));
            fotoRecuperada.setArquivo(imagem.getBytes());
            fotoRecuperada.setTipo(imagem.getContentType());
            fotoRecuperada.setNome(nomeFoto);
            fotoRecuperada.setUsuario(usuario);
        }
        FotoEntity fotoSaved = fotoRepository.save(fotoRecuperada);
        Set<CargoEntity> cargosEntities = usuario.getCargos();
        UsuarioDTO usuarioDTO = objectMapper.convertValue(usuarioService.salvarUsuario(usuario), UsuarioDTO.class);
        usuarioDTO.setCargos(usuarioService.getCargosDTO(cargosEntities));
        usuarioDTO.setImagem(fotoSaved.getArquivo());
        return usuarioDTO;
    }
}
