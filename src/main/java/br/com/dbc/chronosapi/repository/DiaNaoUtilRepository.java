package br.com.dbc.chronosapi.repository;

import br.com.dbc.chronosapi.entity.classes.DiaNaoUtilEntity;
import br.com.dbc.chronosapi.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DiaNaoUtilRepository extends JpaRepository<DiaNaoUtilEntity, Integer> {

    List<DiaNaoUtilEntity> findByRepeticaoAnual(Status status);

    @Query(" SELECT obj " +
            " from DIA_NAO_UTIL obj " +
            " WHERE (:descricao is null or UPPER(obj.descricao) LIKE UPPER(concat('%', :descricao, '%'))) AND " +
            " (:dtFinal is null or :dtInicial is null or obj.dataInicial BETWEEN :dtInicial AND :dtFinal) " +
            " ORDER BY obj.dataInicial "
    )
    Page<DiaNaoUtilEntity> findAllByFiltro(Pageable pageable, String descricao, LocalDate dtFinal, LocalDate dtInicial);

    @Query(" SELECT obj " +
            " FROM DIA_NAO_UTIL obj " +
            " WHERE EXTRACT(YEAR FROM obj.dataInicial) = :ano " +
            " ORDER BY obj.dataInicial"
    )
    Page<DiaNaoUtilEntity> findAllByAno(Pageable pageable, int ano);

    DiaNaoUtilEntity findByDataInicial(LocalDate dataInical);

    @Query(" SELECT obj " +
            " FROM DIA_NAO_UTIL obj " +
            " WHERE (UPPER(obj.descricao) = UPPER(:descricao)) AND (EXTRACT(YEAR FROM obj.dataInicial) = :ano) "
    )
    Page<DiaNaoUtilEntity> findByDescricaoAndAno(Pageable pageable, String descricao, int ano);
}
