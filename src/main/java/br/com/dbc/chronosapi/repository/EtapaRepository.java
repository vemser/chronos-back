package br.com.dbc.chronosapi.repository;

import br.com.dbc.chronosapi.entity.classes.EtapaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtapaRepository extends JpaRepository<EtapaEntity, Integer> {


}
