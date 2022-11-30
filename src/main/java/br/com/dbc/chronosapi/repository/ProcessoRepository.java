package br.com.dbc.chronosapi.repository;

import br.com.dbc.chronosapi.entity.classes.processos.ProcessoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProcessoRepository extends JpaRepository<ProcessoEntity, Integer> {

    Set<ProcessoEntity> findByOrderByOrdemExecucaoAsc();

    @Query("select p " +
            "from PROCESSO p " +
            "join p.etapa e " +
            "join e.processos pr " +
            "where pr.idEtapa = ?1 " +
            "order by p.ordemExecucao")
    Set<ProcessoEntity> findProcessosByOrdemExecucao(Integer idEtapa);

}
