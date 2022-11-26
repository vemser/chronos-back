package br.com.dbc.chronosapi.entity.classes;

import br.com.dbc.chronosapi.entity.enums.TipoAcesso;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name = "PROCESSO")
public class ProcessoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCESSO_SEQ")
    @SequenceGenerator(name = "PROCESSO_SEQ", sequenceName = "SEQ_PROCESSO", allocationSize = 1)
    @Column(name = "ID_PROCESSO")
    private Integer idProcesso;

    @JsonIgnore
    @OneToMany(mappedBy = "processo", fetch = FetchType.LAZY)
    Set<EtapaEntity> etapas;

    @Column(name = "AREA_ENVOLVIDA")
    private TipoAcesso areaEnvolvida;

    @Column(name = "RESPONSAVEL")
    private TipoAcesso responsavel;

    @Column(name = "DURACAO_PROCESSO")
    private LocalDate duracaoProcesso;

    @Column(name = "DIAS_UTEIS")
    private Integer diasUteis;

    // FALTA ORDEM DE PROCESSO

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "PROCESSO_EDICAO",
            joinColumns = @JoinColumn(name = "ID_PROCESSO"),
            inverseJoinColumns = @JoinColumn(name = "ID_EDICAO")
    )
    Set<EdicaoEntity> edicoes;

}
