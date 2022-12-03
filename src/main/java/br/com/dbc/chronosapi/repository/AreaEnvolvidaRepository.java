package br.com.dbc.chronosapi.repository;

import br.com.dbc.chronosapi.entity.classes.processos.AreaEnvolvidaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AreaEnvolvidaRepository extends JpaRepository<AreaEnvolvidaEntity, Integer> {

    @Query("select ae from AREA_ENVOLVIDA ae where upper(ae.nome) = :nome")
    AreaEnvolvidaEntity findByNomeArea(String nome);
}
