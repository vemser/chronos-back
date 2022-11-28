package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.dto.CargoDTO;
import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.usuario.UsuarioCreateDTO;
import br.com.dbc.chronosapi.dto.usuario.UAdminUpdateDTO;
import br.com.dbc.chronosapi.dto.usuario.UsuarioDTO;
import br.com.dbc.chronosapi.dto.usuario.UsuarioUpdateDTO;
import br.com.dbc.chronosapi.entity.classes.CargoEntity;
import br.com.dbc.chronosapi.entity.classes.UsuarioEntity;
import br.com.dbc.chronosapi.entity.enums.StatusUsuario;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
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
    private final EmailService emailService;

    private final LoginService loginService;

    public PageDTO<UsuarioDTO> list(Integer pagina, Integer tamanho) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        Page<UsuarioEntity> paginaDoRepositorio = usuarioRepository.findAll(pageRequest);
        List<UsuarioDTO> usuarios = paginaDoRepositorio.getContent().stream()
                .map(usuario -> {
                    UsuarioDTO usuarioDTO = objectMapper.convertValue(usuario, UsuarioDTO.class);
                    usuarioDTO.setCargos(getCargosDTO(usuario.getCargos()));
                    return usuarioDTO;
                })
                .toList();
        return new PageDTO<>(paginaDoRepositorio.getTotalElements(),
                paginaDoRepositorio.getTotalPages(),
                pagina,
                tamanho,
                usuarios
        );
    }

    public UsuarioDTO uploadImage(Integer idUsuario, MultipartFile imagem) throws RegraDeNegocioException, IOException {
        UsuarioEntity usuario = findById(idUsuario);
        usuario.setImagem(imagem.getBytes());
        return objectMapper.convertValue(usuario, UsuarioDTO.class);
    }

    public UsuarioDTO create(UsuarioCreateDTO usuarioCreateDTO) throws IOException, RegraDeNegocioException {
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        Faker faker = new Faker();
        String senha = faker.internet().password(10, 11, true, true, true);
        String senhaCriptografada = passwordEncoder.encode(senha);
        usuarioEntity.setNome(usuarioCreateDTO.getNome());
        usuarioEntity.setEmail(usuarioCreateDTO.getEmail());
        usuarioEntity.setSenha(senhaCriptografada);
        Set<CargoEntity> cargos = usuarioCreateDTO.getCargos().stream()
                .map(cargo -> (cargoService.findByNome(cargo))).collect(Collectors.toSet());
        usuarioEntity.setCargos(new HashSet<>(cargos));
        usuarioEntity.setStatus(StatusUsuario.ATIVO);
        usuarioEntity.setImagem(null);
        UsuarioDTO usuarioDTO = objectMapper.convertValue(usuarioRepository.save(usuarioEntity), UsuarioDTO.class);
        Set<CargoDTO> cargosDTO = getCargosDTO(cargos);
        usuarioDTO.setCargos(new HashSet<>(cargosDTO));
        emailService.sendRecoverPasswordEmail(usuarioEntity, senha, "Senha para acessar o CHRONOS", "teste.ftl");
        return usuarioDTO;
    }

    public UsuarioDTO updatePerfil(UsuarioUpdateDTO usuarioUpdate) throws IOException, RegraDeNegocioException {
        Integer idLoggedUser = loginService.getIdLoggedUser();

        UsuarioEntity usuarioRecover = findById(idLoggedUser);
        if (passwordEncoder.matches(usuarioUpdate.getSenhaAtual(), usuarioRecover.getPassword())) {
            usuarioRecover.setNome(usuarioUpdate.getNome());
            if (usuarioUpdate.equals(usuarioUpdate.getConfirmacaoNovaSenha())) {
                usuarioRecover.setSenha(passwordEncoder.encode(usuarioUpdate.getNovaSenha()));
                usuarioRepository.save(usuarioRecover);
            } else {
                throw new RegraDeNegocioException("Senhas incompatíveis!");
            }
            UsuarioDTO usuarioDTO = objectMapper.convertValue(usuarioRecover, UsuarioDTO.class);
            Set<CargoEntity> cargosEntities = usuarioRecover.getCargos();
            Set<CargoDTO> cargosDTO = getCargosDTO(cargosEntities);
            usuarioDTO.setCargos(cargosDTO);
            return usuarioDTO;
        } else {
            throw new RegraDeNegocioException("Senha atual inválida!");
        }
    }

    public UsuarioDTO updateAdmin(Integer id, UAdminUpdateDTO usuarioUpdate) throws IOException, RegraDeNegocioException {
        UsuarioEntity usuarioRecover = findById(id);
        usuarioRecover.setNome(usuarioUpdate.getNome());
        Set<CargoEntity> cargos = usuarioUpdate.getCargos().stream()
                .map(cargo -> (cargoService.findByNome(cargo))).collect(Collectors.toSet());
        usuarioRecover.setCargos(new HashSet<>(cargos));
        UsuarioDTO usuarioDTO = objectMapper.convertValue(usuarioRepository.save(usuarioRecover), UsuarioDTO.class);
        Set<CargoDTO> cargosDTO = getCargosDTO(cargos);
        usuarioDTO.setCargos(new HashSet<>(cargosDTO));
        return usuarioDTO;
    }

    public void enableOrDisable(Integer idUsuario) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = this.findById(idUsuario);
        if(usuarioEntity.getStatus() == StatusUsuario.ATIVO) {
            usuarioEntity.setStatus(StatusUsuario.INATIVO);
            usuarioRepository.save(usuarioEntity);
        }else {
            usuarioEntity.setStatus(StatusUsuario.ATIVO);
            usuarioRepository.save(usuarioEntity);
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
