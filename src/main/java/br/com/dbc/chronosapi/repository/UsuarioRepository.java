package br.com.dbc.chronosapi.repository;

import br.com.dbc.chronosapi.entity.classes.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    UsuarioEntity findByEmail (String email);

    Optional<UsuarioEntity> findByEmailAndSenha(String email, String senha);

    List<UsuarioEntity> findByLoginContainingIgnoreCase(String nome);


}
