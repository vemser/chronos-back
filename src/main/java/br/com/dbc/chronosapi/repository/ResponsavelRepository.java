package br.com.dbc.chronosapi.repository;

import br.com.dbc.chronosapi.entity.classes.processos.ResponsavelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ResponsavelRepository extends JpaRepository<ResponsavelEntity, Integer> {

    @Query("select r from RESPONSAVEL r where upper(r.nome) = :nome")
    ResponsavelEntity findByNomeResponsavel (String nome);
}
