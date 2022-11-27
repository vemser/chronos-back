package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.dto.CargoDTO;
import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.usuario.UsuarioDTO;
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

    public UsuarioDTO create(String nome, String email, List<String> stringCargos, MultipartFile imagem) throws IOException, RegraDeNegocioException {
        UsuarioEntity usuarioEntity = new UsuarioEntity();

        String senha = "teste";
        String senhaCriptografada = passwordEncoder.encode(senha);

        usuarioEntity.setNome(nome);
        usuarioEntity.setEmail(email);
        usuarioEntity.setSenha(senhaCriptografada);
        usuarioEntity.setImagem(imagem.getBytes());
        Set<CargoEntity> cargos = stringCargos.stream()
                .map(cargo -> (cargoService.findByNome(cargo))).collect(Collectors.toSet());
        usuarioEntity.setCargos(new HashSet<>(cargos));
        usuarioEntity.setStatus(StatusUsuario.ATIVO);
        UsuarioDTO usuarioDTO = objectMapper.convertValue(usuarioRepository.save(usuarioEntity), UsuarioDTO.class);
        Set<CargoDTO> cargosDTO = getCargosDTO(cargos);
        usuarioDTO.setCargos(new HashSet<>(cargosDTO));
        return usuarioDTO;
    }

    public UsuarioDTO update(Integer id, String nome, String senhaAtual, String novaSenha, String confirmacaoNovaSenha, MultipartFile imagem) throws IOException, RegraDeNegocioException {
        UsuarioEntity usuarioRecover = findById(id);
        if (passwordEncoder.matches(senhaAtual, usuarioRecover.getPassword())) {
            usuarioRecover.setNome(nome);
            usuarioRecover.setImagem(imagem.getBytes());
            if (novaSenha.equals(confirmacaoNovaSenha)) {
                usuarioRecover.setSenha(passwordEncoder.encode(novaSenha));
                usuarioRepository.save(usuarioRecover);
            } else {
                throw new RegraDeNegocioException("Senhas incompatíveis!");
            }
            return objectMapper.convertValue(usuarioRecover, UsuarioDTO.class);
        } else {
            throw new RegraDeNegocioException("Senha atual inválida");
        }
    }
    public void delete(Integer idUsuario) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = this.findById(idUsuario);
        usuarioRepository.delete(usuarioEntity);
    }

    private Set<CargoDTO> getCargosDTO(Set<CargoEntity> cargos) {
        return cargos.stream()
                .map(cargoEntity -> objectMapper.convertValue(cargoEntity, CargoDTO.class))
                .collect(Collectors.toSet());
    }

    public UsuarioEntity findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public UsuarioEntity findById(Integer idUsuario) throws RegraDeNegocioException {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado!"));
    }
}
