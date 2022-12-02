package br.com.dbc.chronosapi.repository;

import br.com.dbc.chronosapi.entity.classes.FotoEntity;
import br.com.dbc.chronosapi.entity.classes.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface FotoRepository extends JpaRepository<FotoEntity, Integer> {
    FotoEntity findByUsuario(UsuarioEntity usuario);
}
