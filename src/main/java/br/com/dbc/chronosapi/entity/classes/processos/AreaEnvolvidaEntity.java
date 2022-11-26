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

    @Column(name = "AREA_ENVOLVIDA")
    private String areaEnvolvida;

    @JsonIgnore
    @OneToMany(mappedBy = "areaEnvolvida", fetch = FetchType.LAZY)
    private Set<ProcessoEntity> processos;

}
