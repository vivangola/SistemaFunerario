DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `listaFuncionario_sp`(IN busca NVARCHAR(100), IN campo INTEGER, IN aux INTEGER)
BEGIN

DECLARE buscaC NVARCHAR(100);
SET buscaC = CONCAT('%',busca,'%');
	
IF aux = 0 THEN

	IF campo = 1 THEN
		SELECT nome, cargo, cpf, rg, telefone, sexo, estadoCivil, DATE_FORMAT(dataNascimento, '%d/%m/%Y') as dataNascimento, endereco, bairro, cidade, estado, cep FROM funcionario 
		WHERE cpf LIKE buscaC;
	ELSEIF campo = 2 THEN
		SELECT nome, cargo, cpf, rg, telefone, sexo, estadoCivil, DATE_FORMAT(dataNascimento, '%d/%m/%Y') as dataNascimento, endereco, bairro, cidade, estado, cep  FROM funcionario 
		WHERE nome LIKE buscaC;
	ELSE
		SELECT nome, cargo, cpf, rg, telefone, sexo, estadoCivil, DATE_FORMAT(dataNascimento, '%d/%m/%Y') as dataNascimento, endereco, bairro, cidade, estado, cep  FROM funcionario;
	END IF;
ELSE
	SELECT nome, cargo, cpf, rg, telefone, sexo, estadoCivil, DATE_FORMAT(dataNascimento, '%d/%m/%Y') as dataNascimento, endereco, bairro, cidade, estado, cep  FROM funcionario 
		WHERE cpf = busca;
END IF;

END$$
DELIMITER ;
