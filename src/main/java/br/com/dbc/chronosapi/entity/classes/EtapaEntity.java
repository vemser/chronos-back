package br.com.dbc.chronosapi.entity.classes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name = "ETAPA")
public class EtapaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ETAPA_SEQ")
    @SequenceGenerator(name = "ETAPA_SEQ", sequenceName = "SEQ_ETAPA", allocationSize = 1)
    @Column(name = "ID_ETAPA")
    private Integer idEtapa;

    @Column(name = "NOME")
    private String nome;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_EDICAO", referencedColumnName = "ID_EDICAO")
    private EdicaoEntity edicao;

    @JsonIgnore
    @OneToMany(mappedBy = "etapa", fetch = FetchType.LAZY)
    private Set<ProcessoEntity> processos;
}
