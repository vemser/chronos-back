package br.com.dbc.chronosapi.repository;

import br.com.dbc.chronosapi.entity.classes.CargoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CargoRepository extends JpaRepository<CargoEntity, Integer> {
    CargoEntity findByNome(String nome);
}
