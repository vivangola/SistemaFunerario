DELIMITER $$
CREATE PROCEDURE `listaPlanos_sp`(IN busca NVARCHAR(14), IN campo INTEGER, IN aux INTEGER)
BEGIN

DECLARE buscaC NVARCHAR(14);
SET buscaC = CONCAT('%',busca,'%');
	
IF aux = 0 THEN

	IF campo = 1 THEN
		SELECT codigo, nome, carencia, valorMensalidade, qtdDependente FROM acesso
		WHERE codigo LIKE buscaC;
	ELSEIF campo = 2 THEN
		SELECT codigo, nome, carencia, valorMensalidade, qtdDependente FROM acesso
		WHERE nome LIKE buscaC;
	ELSE
		SELECT codigo, nome, carencia, valorMensalidade, qtdDependente FROM acesso;
	END IF;
ELSE
	SELECT nome, login, senha, tipo, ativo FROM acesso 
	WHERE login = busca;
END IF;

END$$
DELIMITER ;