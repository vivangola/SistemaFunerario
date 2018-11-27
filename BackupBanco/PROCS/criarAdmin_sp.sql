CREATE DEFINER=`root`@`localhost` PROCEDURE `criarAdmin_sp`()
BEGIN
	INSERT INTO acesso (login, senha, tipo, ativo, fk_cpf) VALUES ('admin','admin',1,1,'123.456.789-10');
    INSERT INTO funcionario (cpf, nome) VALUES ('123.456.789-10','Administrador do Sistema');
END