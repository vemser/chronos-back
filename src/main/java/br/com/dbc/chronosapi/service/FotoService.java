package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.repository.FotoRepository;
import br.com.dbc.chronosapi.entity.classes.FotoEntity;
import br.com.dbc.chronosapi.entity.classes.UsuarioEntity;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FotoService {

    private final UsuarioService usuarioService;
    private final FotoRepository fotoRepository;

    public FotoEntity findById(Integer idPerfil) throws RegraDeNegocioException {
        return fotoRepository.findById(idPerfil)
                .orElseThrow(() -> new RegraDeNegocioException("Imagem não encontrada!"));
    }

    public void arquivarUsuario(MultipartFile file, String email) throws IOException, RegraDeNegocioException {
        UsuarioEntity usuarioEntity = usuarioService.findByEmail(email);

        Optional<FotoEntity> imagemBD = findByUsuario(usuarioEntity);
        String nomeArquivo = StringUtils.cleanPath((file.getOriginalFilename()));
        if (imagemBD.isPresent()) {
            imagemBD.get().setNome(nomeArquivo);
            imagemBD.get().setTipo(file.getContentType());
            imagemBD.get().setArquivo(file.getBytes());
            imagemBD.get().setUsuario(usuarioEntity);
            fotoRepository.save(imagemBD.get());
        } else {
            FotoEntity novaImagemBD = new FotoEntity();
            novaImagemBD.setNome(nomeArquivo);
            novaImagemBD.setTipo(file.getContentType());
            novaImagemBD.setArquivo(file.getBytes());
            novaImagemBD.setUsuario(usuarioEntity);
            fotoRepository.save(novaImagemBD);
        }
    }

    public String pegarImagemUsuario(String email) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = usuarioService.findByEmail(email);
        Optional<FotoEntity> imagemBD = findByUsuario(usuarioEntity);
        if (imagemBD.isEmpty()) {
            throw new RegraDeNegocioException("Usuário não possui imagem cadastrada.");
        }
        return Base64Utils.encodeToString(imagemBD.get().getArquivo());
    }

    private Optional<FotoEntity> findByUsuario(UsuarioEntity usuarioEntity) throws RegraDeNegocioException {
        return fotoRepository.findByUsuario(usuarioEntity);
    }

    public FotoEntity save(FotoEntity foto) {
        return fotoRepository.save(foto);
    }

}
