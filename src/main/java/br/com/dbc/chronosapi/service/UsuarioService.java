package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.usuario.*;
import br.com.dbc.chronosapi.entity.classes.CargoEntity;
import br.com.dbc.chronosapi.entity.classes.FotoEntity;
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

import java.io.IOException;
import java.util.List;
import java.util.Objects;
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
                .map(this::convertToUsuarioDTOAndVerifyFoto)
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
        return convertToUsuarioDTOAndVerifyFoto(usuarioEntity);
    }

    public UsuarioDTO create(UsuarioCreateDTO usuarioCreateDTO) throws IOException, RegraDeNegocioException {
        UsuarioEntity byEmail = findByEmail(usuarioCreateDTO.getEmail());
        if (byEmail != null) {
            throw new RegraDeNegocioException("E-mail cadastrado já existe!");
        }else if (!cargoValido(usuarioCreateDTO.getCargos())){
            throw new RegraDeNegocioException("Insira um cargo válido!");
        }else if(!usuarioCreateDTO.getEmail().trim().endsWith("@dbccompany.com.br")) {
            throw new RegraDeNegocioException("E-mail cadastrado não segue o padrão @dbccompany.com.br");
        }

        UsuarioEntity usuarioEntity = new UsuarioEntity();
        Faker faker = new Faker();
        String senha = faker.internet().password(10, 11, true, true, true);
        String senhaCriptografada = passwordEncoder.encode(senha);
        usuarioEntity.setNome(usuarioCreateDTO.getNome());
        usuarioEntity.setEmail(usuarioCreateDTO.getEmail());
        usuarioEntity.setSenha(senhaCriptografada);
        Set<CargoEntity> cargos = usuarioCreateDTO.getCargos().stream()
                .map(cargo -> (cargoService.findByNome(cargo.getNome()))).collect(Collectors.toSet());
        usuarioEntity.setCargos(cargos);
        usuarioEntity.setStatus(Status.ATIVO);
        UsuarioDTO usuarioDTO = convertToUsuarioDTOAndVerifyFoto(usuarioRepository.save(usuarioEntity));
        Set<CargoDTO> cargosDTO = getCargosDTO(cargos);
        usuarioDTO.setCargos(cargosDTO);
        emailService.sendEmailEnvioSenha(usuarioDTO, senha);
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
            UsuarioDTO usuarioDTO = convertToUsuarioDTOAndVerifyFoto(usuarioRecover);
            Set<CargoEntity> cargosEntities = usuarioRecover.getCargos();
            return getUsuarioDTO(usuarioRecover, usuarioDTO, cargosEntities);
        } else {
            throw new RegraDeNegocioException("Senha atual inválida!");
        }
    }

    private UsuarioDTO getUsuarioDTO(UsuarioEntity usuarioRecover, UsuarioDTO usuarioDTO, Set<CargoEntity> cargosEntities) {
        Set<CargoDTO> cargosDTO = getCargosDTO(cargosEntities);
        usuarioDTO.setCargos(cargosDTO);
        FotoEntity foto = usuarioRecover.getFoto();
        if(foto == null) {
            usuarioDTO.setImagem(null);
        }else {
            usuarioDTO.setImagem(usuarioRecover.getFoto().getArquivo());
        }
        return usuarioDTO;
    }

    public UsuarioDTO updateAdmin(Integer id, UAdminUpdateDTO usuarioUpdate) throws RegraDeNegocioException {

        if (!cargoValido(usuarioUpdate.getCargos())){
            throw new RegraDeNegocioException("Insira um cargo válido!");
        }

        UsuarioEntity usuarioRecover = findById(id);
        usuarioRecover.setNome(usuarioUpdate.getNome());
        Set<CargoEntity> cargos = usuarioUpdate.getCargos().stream()
                .map(cargo -> (cargoService.findByNome(cargo.getNome()))).collect(Collectors.toSet());
        usuarioRecover.setCargos(cargos);
        UsuarioDTO usuarioDTO = convertToUsuarioDTOAndVerifyFoto(usuarioRepository.save(usuarioRecover));

        return getUsuarioDTO(usuarioRecover, usuarioDTO, cargos);
    }

    public boolean cargoValido (Set<CargoCreateDTO> cargoEntities){

        String admin = "ROLE_ADMIN";
        String instrutor = "ROLE_INSTRUTOR";
        String gestor = "ROLE_GESTAO_DE_PESSOAS";

        for (CargoCreateDTO cargo: cargoEntities) {
            if (!Objects.equals(cargo.getNome().trim(), admin) &&
                    !Objects.equals(cargo.getNome().trim(), instrutor) &&
                        !Objects.equals(cargo.getNome().trim(), gestor)) {
                return false;
            }
        }
        return true;
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
        return convertToUsuarioDTOAndVerifyFoto(usuarioEntity);
    }

    public void delete(Integer idUsuario) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = this.findById(idUsuario);
        usuarioRepository.delete(usuarioEntity);
    }

    public Set<CargoDTO> getCargosDTO(Set<CargoEntity> cargos) {
        return cargos.stream()
                .map(this::convertToCargoDTO)
                .collect(Collectors.toSet());
    }

    public UsuarioEntity findByEmail(String email) throws RegraDeNegocioException {
        return usuarioRepository.findByEmail(email);
    }

    public UsuarioEntity findById(Integer idUsuario) throws RegraDeNegocioException {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado!"));
    }

    public UsuarioDTO salvarUsuario(UsuarioEntity usuario) {
        return convertToUsuarioDTOAndVerifyFoto(usuarioRepository.save(usuario));
    }

    private UsuarioDTO convertToUsuarioDTOAndVerifyFoto(UsuarioEntity usuario) {
        UsuarioDTO usuarioDTO = objectMapper.convertValue(usuario, UsuarioDTO.class);
        usuarioDTO.setCargos(getCargosDTO(usuario.getCargos()));
        FotoEntity foto = usuario.getFoto();
        if(foto == null) {
            usuarioDTO.setImagem(null);
        }else {
            usuarioDTO.setImagem(usuario.getFoto().getArquivo());
        }
        return usuarioDTO;
    }

    private CargoDTO convertToCargoDTO(CargoEntity cargoEntity) {
        return objectMapper.convertValue(cargoEntity, CargoDTO.class);
    }
}
