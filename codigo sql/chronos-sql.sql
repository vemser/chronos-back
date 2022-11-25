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
    ATIVO CHAR(1) NOT NULL,
    PRIMARY KEY (ID_USUARIO)
);


CREATE TABLE CARGO (
    ID_CARGO NUMBER NOT NULL,
    NOME VARCHAR2(225) UNIQUE NOT NULL,
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


  
INSERT INTO CARGO (ID_CARGO, NOME)
VALUES (seq_cargo.nextval, 'ROLE_ADMIN'); -- 1

INSERT INTO CARGO (ID_CARGO, NOME)
VALUES (seq_cargo.nextval, 'ROLE_INSTRUTOR'); -- 2

INSERT INTO CARGO (ID_CARGO, NOME)
VALUES (seq_cargo.nextval, 'ROLE_GESTAO_DE_PESSOAS'); -- 3



INSERT INTO CARGO (ID_CARGO, NOME)
VALUES (seq_cargo.nextval, 'ROLE_INSTRUTOR');