package br.com.dbc.chronosapi.repository;

import br.com.dbc.chronosapi.controller.classes.DiaNaoUtilController;
import br.com.dbc.chronosapi.entity.classes.DiaNaoUtilEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DiaNaoUtilRepository extends JpaRepository<DiaNaoUtilEntity, Integer> {

    Optional<DiaNaoUtilEntity> findByDescricao(String descricao);


    @Query(" SELECT obj " +
            " from DIA_NAO_UTIL obj " +
            " WHERE (:descricao is null or UPPER(obj.descricao) LIKE UPPER(concat('%', :descricao, '%'))) AND " +
            " (DIA_NAO_UTIL.dataInicial BETWEEN :dataInicial AND :dataFinal)" +
            " ORDER BY obj.descricao ")
    Page<DiaNaoUtilEntity> findAllByFiltro(Pageable pageable, String descricao, Date dataFinal, Date dataInicial);

}
