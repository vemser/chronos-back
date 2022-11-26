package br.com.dbc.chronosapi.security;

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

    private static final String CHAVE_CARGOS = "CARGOS";
    private static final int VALIDADE_TOKEN_CINCO_MINUTOS = 5;
    private static final int VALIDADE_TOKEN_UM_DIA = 1;

    @Value("${jwt.secret}")
    private String secret;

    public String getToken(UsuarioEntity usuarioEntity, Boolean recuperacao) {

        LocalDateTime dataLocalDateTime = LocalDateTime.now();
        Date date = Date.from(dataLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

        Date dateExperition;

        if(recuperacao) {
            LocalDateTime localDateExperation = dataLocalDateTime.plusMinutes(VALIDADE_TOKEN_CINCO_MINUTOS);
            dateExperition = Date.from(localDateExperation.atZone(ZoneId.systemDefault()).toInstant());
        }else {
            LocalDateTime localDateExperation = dataLocalDateTime.plusDays(VALIDADE_TOKEN_UM_DIA);
            dateExperition = Date.from(localDateExperation.atZone(ZoneId.systemDefault()).toInstant());
        }

        List<String> cargosDoUsuario = usuarioEntity.getCargos().stream()
                .map(CargoEntity::getAuthority)
                .toList();

        return Jwts.builder()
                .setIssuer("vemser-api")
                .claim(Claims.ID, usuarioEntity.getIdUsuario().toString())
                .claim(CHAVE_CARGOS,cargosDoUsuario)
                .setIssuedAt(date)
                .setExpiration(dateExperition)
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
