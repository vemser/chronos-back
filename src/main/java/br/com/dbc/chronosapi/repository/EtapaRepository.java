package br.com.dbc.chronosapi.repository;

import br.com.dbc.chronosapi.entity.classes.EdicaoEntity;
import br.com.dbc.chronosapi.entity.classes.EtapaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface EtapaRepository extends JpaRepository<EtapaEntity, Integer> {

    Set<EtapaEntity> findAllByEdicao(EdicaoEntity edicao);

}
