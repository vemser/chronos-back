package br.com.dbc.chronosapi.repository;

import br.com.dbc.chronosapi.entity.classes.processos.ResponsavelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponsavelRepository extends JpaRepository<ResponsavelEntity, Integer> {

    ResponsavelEntity findByNome (String nome);
}
