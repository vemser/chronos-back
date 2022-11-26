package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.dto.*;
import br.com.dbc.chronosapi.entity.classes.UsuarioEntity;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.repository.UsuarioRepository;
import br.com.dbc.chronosapi.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper objectMapper;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public Integer getIdLoggedUser() {
        return Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }

    public UsuarioDTO getLoggedUser() throws RegraDeNegocioException {
        return objectMapper.convertValue(findById(getIdLoggedUser()), UsuarioDTO.class);
    }

    public UsuarioDTO create(UsuarioCreateDTO usuario) {
        UsuarioEntity usuarioEntity = objectMapper.convertValue(usuario, UsuarioEntity.class);
        String senha = "vEmSeR10@2022";
        String senhaCriptografada = passwordEncoder.encode(senha);
        usuarioEntity.setSenha(senhaCriptografada);
        // Fazer Foto -Pesquisar como fazer uploados de fotos no banco de dados.
        // Fazer Cargo - Esperando resposta do Maicon
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

    public PageDTO<UsuarioDTO> list(Integer pagina, Integer tamanho){
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        Page<UsuarioEntity> paginaRepository = usuarioRepository.findAll(pageRequest);

        List<UsuarioDTO> usuariosDaPagina = paginaRepository.getContent().stream()
                .map(this::getUsuarioDTO)
                .toList();

        return new PageDTO<>(paginaRepository.getTotalElements(), paginaRepository.getTotalPages(), pagina, tamanho, usuariosDaPagina);
    }

    public Optional<UsuarioEntity> findByEmail(String email) throws RegraDeNegocioException {
        return usuarioRepository.findByEmail(email);
    }

    public UsuarioEntity findById(Integer idUsuario) throws RegraDeNegocioException {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado!"));
    }

    @NotNull
    private UsuarioDTO getUsuarioDTO(UsuarioEntity usuarioEntity) {
        UsuarioDTO usuarioDTO = objectMapper.convertValue(usuarioEntity, UsuarioDTO.class);
        usuarioDTO.setCargos(usuarioEntity.getCargos().stream()
                .map(x -> objectMapper.convertValue(x, CargoDTO.class))
                .toList());
        return usuarioDTO;
    }
}
