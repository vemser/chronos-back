package br.com.dbc.chronosapi.repository;

import br.com.dbc.chronosapi.entity.classes.EdicaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EdicaoRepository extends JpaRepository<EdicaoEntity, Integer> {

}
