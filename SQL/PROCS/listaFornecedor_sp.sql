DELIMITER $$
CREATE PROCEDURE `listaFornecedor_sp`(IN busca NVARCHAR(100), IN campo INTEGER, IN aux INTEGER)
BEGIN

DECLARE buscaC NVARCHAR(100);
SET buscaC = CONCAT('%',busca,'%');
	
IF aux = 0 THEN

	IF campo = 1 THEN
		SELECT nome, cnpj, endereco, bairro, cidade, estado, cep, email, telefone, inscricaoEstadual FROM fornecedor
		WHERE cnpj LIKE buscaC;
	ELSEIF campo = 2 THEN
		SELECT nome, cnpj, endereco, bairro, cidade, estado, cep, email, telefone, inscricaoEstadual FROM fornecedor
		WHERE nome LIKE buscaC;
	ELSE
		SELECT nome, cnpj, endereco, bairro, cidade, estado, cep, email, telefone, inscricaoEstadual FROM fornecedor;
	END IF;
ELSE
	SELECT nome, cnpj, endereco, bairro, cidade, estado, cep, email, telefone, inscricaoEstadual FROM fornecedor
	WHERE cnpj = busca;
END IF;

END$$
DELIMITER ;