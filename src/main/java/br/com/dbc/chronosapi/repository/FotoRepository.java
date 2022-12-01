package br.com.dbc.chronosapi.repository;

import br.com.dbc.chronosapi.entity.classes.FotoEntity;
import br.com.dbc.chronosapi.entity.classes.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface FotoRepository extends JpaRepository<FotoEntity, Integer> {
    Optional<FotoEntity> findByUsuario(UsuarioEntity usuarioEntity);
}
