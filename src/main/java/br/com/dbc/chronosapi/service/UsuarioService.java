package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.usuario.*;
import br.com.dbc.chronosapi.entity.classes.CargoEntity;
import br.com.dbc.chronosapi.entity.classes.UsuarioEntity;
import br.com.dbc.chronosapi.entity.enums.Status;
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

    public UsuarioDTO buscarUsuarioLogado() throws RegraDeNegocioException {
        UsuarioDTO loggedUser = loginService.getLoggedUser();
        UsuarioEntity usuarioEntity = findById(loggedUser.getIdUsuario());
        UsuarioDTO usuarioDTO = objectMapper.convertValue(usuarioEntity, UsuarioDTO.class);
        usuarioDTO.setCargos(getCargosDTO(usuarioEntity.getCargos()));
        usuarioDTO.setImagem(usuarioEntity.getImagem());
        return usuarioDTO;
    }

    public UsuarioDTO uploadImage(Integer idUsuario, MultipartFile imagem) throws RegraDeNegocioException, IOException {
        UsuarioEntity usuario = findById(idUsuario);
        usuario.setImagem(imagem.getBytes());
        Set<CargoEntity> cargosEntities = usuario.getCargos();
        UsuarioDTO usuarioDTO = objectMapper.convertValue(usuarioRepository.save(usuario), UsuarioDTO.class);
        usuarioDTO.setCargos(getCargosDTO(cargosEntities));
        return usuarioDTO;
    }

    public UsuarioDTO uploadImagePerfil(MultipartFile imagem) throws RegraDeNegocioException, IOException {
        Integer idLoggedUser = loginService.getIdLoggedUser();

        UsuarioEntity usuario = findById(idLoggedUser);
        usuario.setImagem(imagem.getBytes());
        Set<CargoEntity> cargosEntities = usuario.getCargos();
        UsuarioDTO usuarioDTO = objectMapper.convertValue(usuarioRepository.save(usuario), UsuarioDTO.class);
        usuarioDTO.setCargos(getCargosDTO(cargosEntities));
        return usuarioDTO;
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
        usuarioEntity.setCargos(cargos);
        usuarioEntity.setStatus(Status.ATIVO);
        usuarioEntity.setImagem(null);
        UsuarioDTO usuarioDTO = objectMapper.convertValue(usuarioRepository.save(usuarioEntity), UsuarioDTO.class);
        Set<CargoDTO> cargosDTO = getCargosDTO(cargos);
        usuarioDTO.setCargos(cargosDTO);
        emailService.sendEmailEnvioSenha(usuarioDTO.getEmail(), senha);
        return usuarioDTO;
    }

    public UsuarioDTO updatePerfil(UsuarioUpdateDTO usuarioUpdate) throws RegraDeNegocioException {
        Integer idLoggedUser = loginService.getIdLoggedUser();

        UsuarioEntity usuarioRecover = findById(idLoggedUser);
        if (passwordEncoder.matches(usuarioUpdate.getSenhaAtual(), usuarioRecover.getPassword())) {
            usuarioRecover.setNome(usuarioUpdate.getNome());
            if (usuarioUpdate.getNovaSenha().equals(usuarioUpdate.getConfirmacaoNovaSenha())) {
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

    public UsuarioDTO updateAdmin(Integer id, UAdminUpdateDTO usuarioUpdate) throws RegraDeNegocioException {
        UsuarioEntity usuarioRecover = findById(id);
        usuarioRecover.setNome(usuarioUpdate.getNome());
        Set<CargoEntity> cargos = usuarioUpdate.getCargos().stream()
                .map(cargo -> (cargoService.findByNome(cargo))).collect(Collectors.toSet());
        usuarioRecover.setCargos(cargos);
        UsuarioDTO usuarioDTO = objectMapper.convertValue(usuarioRepository.save(usuarioRecover), UsuarioDTO.class);
        Set<CargoDTO> cargosDTO = getCargosDTO(cargos);
        usuarioDTO.setCargos(cargosDTO);
        return usuarioDTO;
    }

    public UsuarioDTO enableOrDisable(Integer idUsuario) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = this.findById(idUsuario);
        if(usuarioEntity.getStatus() == Status.ATIVO) {
            usuarioEntity.setStatus(Status.INATIVO);
            usuarioRepository.save(usuarioEntity);
        }else {
            usuarioEntity.setStatus(Status.ATIVO);
            usuarioRepository.save(usuarioEntity);
        }
        return objectMapper.convertValue(usuarioEntity, UsuarioDTO.class);
    }

    public void delete(Integer idUsuario) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = this.findById(idUsuario);
        usuarioRepository.delete(usuarioEntity);
    }

    public Set<CargoDTO> getCargosDTO(Set<CargoEntity> cargos) {
        return cargos.stream()
                .map(cargoEntity -> objectMapper.convertValue(cargoEntity, CargoDTO.class))
                .collect(Collectors.toSet());
    }

    public UsuarioEntity findByEmail(String email) throws RegraDeNegocioException {
        return usuarioRepository.findByEmail(email);
    }

    public UsuarioEntity findById(Integer idUsuario) throws RegraDeNegocioException {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado!"));
    }
}
