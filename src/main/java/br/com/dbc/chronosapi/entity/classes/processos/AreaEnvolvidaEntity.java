package br.com.dbc.chronosapi.entity.classes.processos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name = "AREA_ENVOLVIDA")
public class AreaEnvolvidaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AREA_ENVOLVIDA_SEQ")
    @SequenceGenerator(name = "AREA_ENVOLVIDA_SEQ", sequenceName = "SEQ_AREA_ENVOLVIDA", allocationSize = 1)
    @Column(name = "ID_AREA_ENVOLVIDA")
    private Integer idAreaEnvolvida;

    @Column(name = "NOME")
    private String nome;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "PROCESSO_AREA_ENVOLVIDA",
            joinColumns = @JoinColumn(name = "ID_AREA_ENVOLVIDA"),
            inverseJoinColumns = @JoinColumn(name = "ID_PROCESSO")
    )
    private Set<ProcessoEntity> processos;
}
