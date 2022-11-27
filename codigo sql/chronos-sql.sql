CREATE USER CHRONOS IDENTIFIED BY oracle;
GRANT CONNECT TO CHRONOS;
GRANT CONNECT, RESOURCE, DBA TO CHRONOS;
GRANT CREATE SESSION TO CHRONOS;
GRANT DBA TO CHRONOS;
GRANT CREATE VIEW, CREATE PROCEDURE, CREATE SEQUENCE to CHRONOS;
GRANT UNLIMITED TABLESPACE TO CHRONOS;
GRANT CREATE MATERIALIZED VIEW TO CHRONOS;
GRANT CREATE TABLE TO CHRONOS;
GRANT GLOBAL QUERY REWRITE TO CHRONOS;
GRANT SELECT ANY TABLE TO CHRONOS;

CREATE TABLE USUARIO (
                         ID_USUARIO NUMBER NOT NULL,
                         NOME VARCHAR2(225) NOT NULL,
                         EMAIL VARCHAR2(225) UNIQUE NOT NULL,
                         SENHA VARCHAR2(225) NOT NULL,
                         IMAGEM BLOB NOT NULL,
                         STATUS NUMBER NOT NULL,
                         PRIMARY KEY (ID_USUARIO)
);

CREATE TABLE EDICAO (
                        ID_EDICAO NUMBER NOT NULL,
                        NOME VARCHAR2(225) NOT NULL,
                        DATA_INICIAL DATE  NOT NULL,
                        DATA_FINAL DATE NOT NULL,
                        PRIMARY KEY (ID_EDICAO)
);

CREATE TABLE ETAPA (
                       ID_ETAPA NUMBER NOT NULL,
                       NOME VARCHAR2(225) NOT NULL,
                       PRIMARY KEY (ID_ETAPA)
);

CREATE TABLE AREA_ENVOLVIDA (
                                ID_AREA_ENVOLVIDA NUMBER NOT NULL,
                                NOME VARCHAR2(225),
                                PRIMARY KEY (ID_AREA_ENVOLVIDA)
);


CREATE TABLE RESPONSAVEL (
                             ID_RESPONSAVEL NUMBER NOT NULL,
                             NOME VARCHAR2(225),
                             PRIMARY KEY (ID_RESPONSAVEL)
);

CREATE TABLE PROCESSO (
                          ID_PROCESSO NUMBER NOT NULL,
                          ID_ETAPA NUMBER NOT NULL,
                          ID_AREA_ENVOLVIDA NUMBER NOT NULL,
                          ID_RESPONSAVEL NUMBER NOT NULL,
                          DURACAO_PROCESSO VARCHAR2(225) NOT NULL,
                          DIAS_UTEIS NUMBER NOT NULL,
                          ORDEM_EXECUCAO NUMBER NOT NULL,
                          PRIMARY KEY (ID_PROCESSO),
                          CONSTRAINT FK_AREA_ENVOLVIDA FOREIGN KEY (ID_AREA_ENVOLVIDA) REFERENCES AREA_ENVOLVIDA (ID_AREA_ENVOLVIDA),
                          CONSTRAINT FK_RESPONSAVEL FOREIGN KEY (ID_RESPONSAVEL) REFERENCES RESPONSAVEL (ID_RESPONSAVEL),
                          CONSTRAINT FK_PROCESSO_ETAPA FOREIGN KEY (ID_ETAPA) REFERENCES ETAPA (ID_ETAPA)
);


CREATE TABLE PROCESSO_EDICAO (
                                 ID_PROCESSO NUMBER NOT NULL,
                                 ID_EDICAO NUMBER NOT NULL,
                                 PRIMARY KEY (ID_PROCESSO, ID_EDICAO),
                                 CONSTRAINT FK_PROCESSO_EDICAO_EDICAO FOREIGN KEY (ID_EDICAO) REFERENCES EDICAO (ID_EDICAO),
                                 CONSTRAINT FK_PROCESSO_EDICAO_PROCESSO FOREIGN KEY (ID_PROCESSO) REFERENCES PROCESSO (ID_PROCESSO)
);


CREATE TABLE CARGO (
                       ID_CARGO NUMBER NOT NULL,
                       NOME VARCHAR2(225) UNIQUE NOT NULL,
                       DESCRICAO VARCHAR2(225) UNIQUE NOT NULL,
                       PRIMARY KEY (ID_CARGO)
);

CREATE TABLE USUARIO_CARGO (
                               ID_USUARIO NUMBER NOT NULL,
                               ID_CARGO NUMBER NOT NULL,
                               PRIMARY KEY(ID_USUARIO, ID_CARGO),
                               CONSTRAINT FK_USUARIO_CARGO_CARGO FOREIGN KEY (ID_CARGO) REFERENCES CARGO (ID_CARGO),
                               CONSTRAINT FK_USUARIO_CARGO_USUARIO FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO (ID_USUARIO)
);


CREATE SEQUENCE SEQ_USUARIO
    START WITH 1
    INCREMENT BY 1
    NOCACHE NOCYCLE;

CREATE SEQUENCE SEQ_CARGO
    START WITH     1
    INCREMENT BY   1
    NOCACHE NOCYCLE;

CREATE SEQUENCE SEQ_EDICAO
    START WITH     1
    INCREMENT BY   1
    NOCACHE NOCYCLE;

CREATE SEQUENCE SEQ_ETAPA
    START WITH     1
    INCREMENT BY   1
    NOCACHE NOCYCLE;

CREATE SEQUENCE SEQ_PROCESSO
    START WITH     1
    INCREMENT BY   1
    NOCACHE NOCYCLE;

CREATE SEQUENCE SEQ_AREA_ENVOLVIDA
    START WITH     1
    INCREMENT BY   1
    NOCACHE NOCYCLE;

CREATE SEQUENCE SEQ_RESPONSAVEL
    START WITH     1
    INCREMENT BY   1
    NOCACHE NOCYCLE;

INSERT INTO CARGO (ID_CARGO, NOME, DESCRICAO)
VALUES (seq_cargo.nextval, 'ROLE_ADMIN', 'Administrador'); -- 1

INSERT INTO CARGO (ID_CARGO, NOME, DESCRICAO)
VALUES (seq_cargo.nextval, 'ROLE_INSTRUTOR', 'Instrutor'); -- 2

INSERT INTO CARGO (ID_CARGO, NOME, DESCRICAO)
VALUES (seq_cargo.nextval, 'ROLE_GESTAO_DE_PESSOAS', 'Gestão de pessoas'); -- 3

