package br.com.dbc.chronosapi.entity.classes.processos;

import br.com.dbc.chronosapi.entity.classes.EtapaEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ETAPA", referencedColumnName = "ID_ETAPA")
    private EtapaEntity etapa;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_AREA_ENVOLVIDA", referencedColumnName = "ID_AREA_ENVOLVIDA")
    private AreaEnvolvidaEntity areaEnvolvida;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_RESPONSAVEL", referencedColumnName = "ID_RESPONSAVEL")
    private ResponsavelEntity responsavel;

    @Column(name = "DURACAO_PROCESSO")
    private String duracaoProcesso;

    @Column(name = "ORDEM_EXECUCAO")
    private Integer OrdemExecucao;

    @Column(name = "DIAS_UTEIS")
    private Integer diasUteis;


}
