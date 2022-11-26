package br.com.dbc.chronosapi.security;

import br.com.dbc.chronosapi.entity.classes.UsuarioEntity;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final UsuarioService usuarioService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            return usuarioService.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Email inválido!"));
    }
}
