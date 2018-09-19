DELIMITER $$
CREATE PROCEDURE `listaConta_sp`(IN busca NVARCHAR(14), IN campo INTEGER, IN aux INTEGER)
BEGIN

DECLARE buscaC NVARCHAR(14);
SET buscaC = CONCAT('%',busca,'%');
	
IF aux = 0 THEN

	IF campo = 1 THEN
		SELECT codigo, dataInclusao, situacao, vencimentoMensalidade
		FROM conta
		WHERE login LIKE buscaC and codigo <> 0;
	ELSEIF campo = 2 THEN
		SELECT codigo, dataInclusao, situacao, vencimentoMensalidade 
		FROM conta 
		WHERE nome LIKE buscaC and codigo <> 0;
	ELSE
		SELECT codigo, dataInclusao, situacao, vencimentoMensalidade
		FROM conta WHERE codigo <> 0;
	END IF;
ELSE
	SELECT codigo, dataInclusao, situacao, vencimentoMensalidade
		FROM conta 
		WHERE login = busca and codigo <> 0;
END IF;

END$$
DELIMITER ;



