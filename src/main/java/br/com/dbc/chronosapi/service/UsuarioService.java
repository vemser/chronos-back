package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.UsuarioCreateDTO;
import br.com.dbc.chronosapi.dto.UsuarioDTO;
import br.com.dbc.chronosapi.dto.UsuarioUpdateDTO;
import br.com.dbc.chronosapi.entity.classes.CargoEntity;
import br.com.dbc.chronosapi.entity.classes.UsuarioEntity;
import br.com.dbc.chronosapi.entity.enums.StatusUsuario;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper objectMapper;
    private final CargoService cargoService;
    private final PasswordEncoder passwordEncoder;

    public PageDTO<UsuarioDTO> list(Integer pagina, Integer tamanho) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        Page<UsuarioEntity> paginaDoRepositorio = usuarioRepository.findAll(pageRequest);
        List<UsuarioDTO> usuarios = paginaDoRepositorio.getContent().stream()
                .map(usuario -> objectMapper.convertValue(usuario, UsuarioDTO.class))
                .toList();
        return new PageDTO<>(paginaDoRepositorio.getTotalElements(),
                paginaDoRepositorio.getTotalPages(),
                pagina,
                tamanho,
                usuarios
        );
    }

    public UsuarioDTO create(UsuarioCreateDTO usuario, MultipartFile imagem) throws IOException, RegraDeNegocioException {
        UsuarioEntity usuarioEntity = objectMapper.convertValue(usuario, UsuarioEntity.class);
        String senha = "teste";
        String senhaCriptografada = passwordEncoder.encode(senha);
        usuarioEntity.setSenha(senhaCriptografada);
        usuarioEntity.setImagem(imagem.getBytes());
        Set<CargoEntity> cargos = usuario.getCargos().stream()
                .map(cargo -> cargoService.findByNome(cargo)).collect(Collectors.toSet());
        usuarioEntity.setCargos(new HashSet<>(cargos));
        usuarioEntity.setStatus(StatusUsuario.ATIVO);
        return objectMapper.convertValue(usuarioRepository.save(usuarioEntity), UsuarioDTO.class);
    }

    public UsuarioDTO update(Integer id, UsuarioUpdateDTO usuarioAtualizar) throws RegraDeNegocioException {
        UsuarioEntity usuarioEncontrado = findById(id);
        usuarioEncontrado.setNome(usuarioAtualizar.getNome());

        if(usuarioAtualizar.getNovaSenha().equals(usuarioAtualizar.getCorfirmacaoNovaSenha())) {
            usuarioEncontrado.setSenha(passwordEncoder.encode(usuarioAtualizar.getNovaSenha()));
            usuarioRepository.save(usuarioEncontrado);
        } else {
           throw new RegraDeNegocioException("Senhas incompatíveis!");
        }

//        UsuarioDto usuarioDto = objectMapper.convertValue(usuarioEncontrado, UsuarioDto.class);

        return null;
    }

    public void delete(Integer idUsuario) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = this.findById(idUsuario);
        usuarioRepository.delete(usuarioEntity);
    }

    public UsuarioEntity findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public UsuarioEntity findById(Integer idUsuario) throws RegraDeNegocioException {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado!"));
    }
}
