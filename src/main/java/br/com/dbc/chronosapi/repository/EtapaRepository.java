package br.com.dbc.chronosapi.repository;

import br.com.dbc.chronosapi.entity.classes.EtapaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtapaRepository extends JpaRepository<EtapaEntity, Integer> {



    Page<EtapaEntity> findAllByOrOrderByOrdemExecucao(Pageable pageable);

}
