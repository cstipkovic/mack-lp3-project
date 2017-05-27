-- To Connect DB 
connect 'jdbc:derby://localhost:1527/meuDB;create=true;user=me;password=pass';

--  Create DB
create table TB_USUARIO(
	usuario_id integer not null,
	nome varchar(15),
	sobrenome varchar(15),
	primary key (usuario_id)
);

-- Colocando sequence
CREATE SEQUENCE USUARIO_ID_SEQUENCE AS INTEGER START WITH 1 INCREMENT BY 1 NO MAXVALUE;

-- Insert de usuarios
INSERT INTO TB_USUARIO (usuario_id, nome, sobrenome) VALUES(NEXT VALUE FOR USUARIO_ID_SEQUENCE, ‘ROGER’, ’FEDERER’);
INSERT INTO TB_USUARIO (usuario_id, nome, sobrenome) VALUES(NEXT VALUE FOR USUARIO_ID_SEQUENCE, ‘STEVE’, ’ROGERS’);
INSERT INTO TB_USUARIO (usuario_id, nome, sobrenome) VALUES(NEXT VALUE FOR USUARIO_ID_SEQUENCE, ‘TONY’, ’STARK’);
INSERT INTO TB_USUARIO (usuario_id, nome, sobrenome) VALUES(NEXT VALUE FOR USUARIO_ID_SEQUENCE, ‘NICK’, ’FURY’);

-- Select para conferir se os dados estão ok
SELECT * FROM TB_USUARIO;
