DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `listaAcesso_sp`(IN busca NVARCHAR(14), IN campo INTEGER, IN aux INTEGER)
BEGIN

DECLARE buscaC NVARCHAR(14);
SET buscaC = CONCAT('%',busca,'%');
	
IF aux = 0 THEN

	IF campo = 1 THEN
		SELECT nome, login, CASE tipo WHEN 1 THEN 'Administrador' ELSE 'Funcionário' END AS tipo, CASE ativo WHEN 1 THEN 'Ativo' ELSE 'Inativo' END AS ativo 
		FROM acesso inner join funcionario on cpf = fk_cpf
		WHERE login LIKE buscaC;
	ELSEIF campo = 2 THEN
		SELECT nome, login, CASE tipo WHEN 1 THEN 'Administrador' ELSE 'Funcionário' END AS tipo, CASE ativo WHEN 1 THEN 'Ativo' ELSE 'Inativo' END AS ativo 
		FROM acesso inner join funcionario on cpf = fk_cpf
		WHERE nome LIKE buscaC;
	ELSE
		SELECT nome, login, CASE tipo WHEN 1 THEN 'Administrador' ELSE 'Funcionário' END AS tipo, CASE ativo WHEN 1 THEN 'Ativo' ELSE 'Inativo' END AS ativo 
		FROM acesso inner join funcionario on cpf = fk_cpf;
	END IF;
ELSE
	SELECT nome, login, senha, tipo, ativo
		FROM acesso inner join funcionario on cpf = fk_cpf
		WHERE login = busca;
END IF;

END$$
DELIMITER ;
