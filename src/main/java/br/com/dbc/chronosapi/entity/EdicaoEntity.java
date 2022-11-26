package br.com.dbc.chronosapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name = "EDICAO")
public class EdicaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EDICAO_SEQ")
    @SequenceGenerator(name = "EDICAO_SEQ", sequenceName = "SEQ_EDICAO", allocationSize = 1)
    @Column(name = "ID_EDICAO")
    private Integer idEdicao;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "DATA_INICIAL")
    private LocalDate dataInicial;

    @Column(name = "DATA_FINAL")
    private LocalDate dataFinal;

    @OneToMany(mappedBy = "edicao", fetch = FetchType.LAZY)
    Set<EtapaEntity> etapas;

}
