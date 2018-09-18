DELIMITER $$
CREATE PROCEDURE `listaPlanos_sp`(IN busca NVARCHAR(14), IN campo INTEGER, IN aux INTEGER)
BEGIN

DECLARE buscaC NVARCHAR(14);
SET buscaC = CONCAT('%',busca,'%');
	
IF aux = 0 THEN

	IF campo = 1 THEN
		SELECT codigo, nome, carencia, valorMensalidade, qtdDependente FROM plano
		WHERE codigo LIKE buscaC and codigo <> 0;
	ELSEIF campo = 2 THEN
		SELECT codigo, nome, carencia, valorMensalidade, qtdDependente FROM plano
		WHERE nome LIKE buscaC and codigo <> 0;
	ELSE
		SELECT codigo, nome, carencia, valorMensalidade, qtdDependente FROM plano
		WHERE codigo <> 0;
	END IF;
ELSE
	SELECT codigo, nome, carencia, valorMensalidade, qtdDependente FROM plano 
	WHERE codigo = busca and codigo <> 0;
END IF;

END$$
DELIMITER ;