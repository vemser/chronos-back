package br.com.dbc.chronosapi.entity.classes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "FOTO")
public class FotoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FOTO")
    @SequenceGenerator(name = "SEQ_FOTO", sequenceName = "SEQ_FOTO", allocationSize = 1)
    @Column(name="ID_FOTO")
    private Integer idFoto;

    @Column(name = "nome")
    private String nome;

    @Column(name = "tipo")
    private String tipo;

    @Lob
    @Column(name = "arquivo")
    private byte[] arquivo;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO")
    private UsuarioEntity usuario;

}
