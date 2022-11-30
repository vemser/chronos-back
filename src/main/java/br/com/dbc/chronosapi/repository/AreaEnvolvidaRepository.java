package br.com.dbc.chronosapi.repository;

import br.com.dbc.chronosapi.entity.classes.processos.AreaEnvolvidaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaEnvolvidaRepository extends JpaRepository<AreaEnvolvidaEntity, Integer> {

    AreaEnvolvidaEntity findByNome (String nome);

    AreaEnvolvidaEntity findByNomeContainingIgnoreCase(String nome);
}
