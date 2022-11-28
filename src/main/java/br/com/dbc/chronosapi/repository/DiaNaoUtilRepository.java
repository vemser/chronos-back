package br.com.dbc.chronosapi.repository;

import br.com.dbc.chronosapi.entity.classes.DiaNaoUtilEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaNaoUtilRepository extends JpaRepository<DiaNaoUtilEntity, Integer> {
}
