package br.com.dbc.chronosapi.entity.enums;

public enum Atividade {
    ATIVO("S"),
    INATIVO("N");

    private final String descricao;

    Atividade(String descricao) {
        this.descricao = descricao;
    }

    public String toString() {
        return descricao;
    }
}
