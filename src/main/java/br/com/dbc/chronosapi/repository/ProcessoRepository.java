package br.com.dbc.chronosapi.repository;

import br.com.dbc.chronosapi.entity.classes.ProcessoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessoRepository extends JpaRepository<ProcessoEntity, Integer> {
}
