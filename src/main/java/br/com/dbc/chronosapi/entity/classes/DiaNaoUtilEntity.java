package br.com.dbc.chronosapi.entity.classes;

import br.com.dbc.chronosapi.entity.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "DIA_NAO_UTIL")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DiaNaoUtilEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DIA_NAO_UTIL_SEQ")
    @SequenceGenerator(name = "DIA_NAO_UTIL_SEQ", sequenceName = "SEQ_DIA_NAO_UTIL", allocationSize = 1)
    @Column(name = "ID_DIA_NAO_UTIL")
    private Integer idDiaNaoUtil;

    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "DATA_INICIAL")
    private LocalDate dataInicial;

    @Column(name = "DATA_FINAL")
    private LocalDate dataFinal;

    @Column(name = "REPETICAO_ANUAL")
    private Status repeticaoAnual;

}
