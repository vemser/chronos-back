package br.com.dbc.chronosapi.service;

import br.com.dbc.chronosapi.dto.CargoDTO;
import br.com.dbc.chronosapi.dto.PageDTO;
import br.com.dbc.chronosapi.dto.UsuarioDTO;
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

    public Optional<UsuarioEntity> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public UsuarioEntity findById(Integer idUsuario) throws RegraDeNegocioException {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado!"));
    }

    public PageDTO<UsuarioDTO> list(Integer pagina, Integer tamanho){
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        Page<UsuarioEntity> paginaRepository = usuarioRepository.findAll(pageRequest);

        List<UsuarioDTO> usuariosDaPagina = paginaRepository.getContent().stream()
                .map(this::getUsuarioDTO)
                .toList();

        return new PageDTO<>(paginaRepository.getTotalElements(), paginaRepository.getTotalPages(), pagina, tamanho, usuariosDaPagina);
    }

    public UsuarioDTO getLoggedUser() throws RegraDeNegocioException {

        return getUsuarioDTO(findById(getIdLoggedUser()));
    }

    public Integer getIdLoggedUser() {
        return Integer.parseInt(String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
    }

    @NotNull
    private UsuarioDTO getUsuarioDTO(UsuarioEntity usuarioEntity) {
        UsuarioDTO dto = objectMapper.convertValue(usuarioEntity, UsuarioDTO.class);
        dto.setCargos(usuarioEntity.getCargos().stream()
                .map(x -> objectMapper.convertValue(x, CargoDTO.class))
                .toList());

        return dto;
    }

}
