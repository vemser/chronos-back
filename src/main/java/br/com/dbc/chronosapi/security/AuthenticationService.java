package br.com.dbc.chronosapi.security;

import br.com.dbc.chronosapi.entity.classes.UsuarioEntity;
import br.com.dbc.chronosapi.exceptions.RegraDeNegocioException;
import br.com.dbc.chronosapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final UsuarioService usuarioService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UsuarioEntity usuario = null;
        try {
            usuario = usuarioService.findByEmail(email);
        } catch (RegraDeNegocioException e) {
            throw new RuntimeException(e);
        }
        if(usuario == null) {
            throw new UsernameNotFoundException("Usuario inválido");
        } else {
            return usuario;
        }
    }
}
