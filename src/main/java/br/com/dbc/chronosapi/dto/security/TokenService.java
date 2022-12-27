package br.com.dbc.chronosapi.dto.security;

import br.com.dbc.chronosapi.entity.classes.CargoEntity;
import br.com.dbc.chronosapi.entity.classes.UsuarioEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final String CHAVE_CARGOS = "cargos";
    private static final String CHAVE_LOGIN = "login";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    public String getToken(UsuarioEntity usuarioEntity) {

        LocalDateTime localDateExpiration = LocalDateTime.now().plusDays(expiration);
        Date dateExpiration = Date.from(localDateExpiration.atZone(ZoneId.systemDefault()).toInstant());

        List<String> cargosDoUsuario = usuarioEntity.getCargos().stream()
                .map(CargoEntity::getAuthority)
                .toList();

        return Jwts.builder()
                .claim(CHAVE_LOGIN, usuarioEntity.getUsername())
                .claim(Claims.ID, usuarioEntity.getIdUsuario().toString())
                .claim(CHAVE_CARGOS, cargosDoUsuario)
                .setIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(dateExpiration)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public UsernamePasswordAuthenticationToken isValid(String token) {
        if (token == null) {
            return null;
        }

        token = token.replace("Bearer ", "");

        Claims chaves = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        String idUsuario = chaves.get(Claims.ID, String.class);

        List<String> cargos = chaves.get(CHAVE_CARGOS, List.class);

        List<SimpleGrantedAuthority> cargosList = cargos.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();


        return new UsernamePasswordAuthenticationToken(idUsuario,
                null, cargosList);
    }
}
