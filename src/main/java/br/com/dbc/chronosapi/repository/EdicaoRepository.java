package br.com.dbc.chronosapi.repository;

import br.com.dbc.chronosapi.entity.classes.EdicaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EdicaoRepository extends JpaRepository<EdicaoEntity, Integer> {
    @Query(" select e from EDICAO e " +
            " where e.status = 1 " +
            " order by e.dataInicial")
    List<EdicaoEntity> findByEdicoesAtivasOrderByDataInicial();
}
