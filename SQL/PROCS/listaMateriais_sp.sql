DELIMITER $$
CREATE PROCEDURE `listaMateriais_sp`(IN busca NVARCHAR(100), IN campo INTEGER, IN aux INTEGER)
BEGIN

DECLARE buscaC NVARCHAR(100);
SET buscaC = CONCAT('%',busca,'%');
	
IF aux = 0 THEN

	IF campo = 1 THEN
		SELECT codigo, nome, modelo, tamanho, case categoria WHEN 1 THEN 'Convalescença' END AS categoria, qtdMinima, estoque FROM material
		WHERE codigo LIKE buscaC and codigo <> 0;
	ELSEIF campo = 2 THEN
		SELECT codigo, nome, modelo, tamanho, case categoria WHEN 1 THEN 'Convalescença' END AS categoria, qtdMinima, estoque FROM material
		WHERE nome LIKE buscaC and codigo <> 0;
	ELSEIF campo = 3 THEN
		SELECT codigo, nome, modelo, tamanho, case categoria WHEN 1 THEN 'Convalescença' END AS categoria, qtdMinima, estoque FROM material
		WHERE categoria LIKE buscaC and codigo <> 0;
	ELSE
		SELECT codigo, nome, modelo, tamanho, case categoria WHEN 1 THEN 'Convalescença' END AS categoria, qtdMinima, estoque FROM material 
		WHERE codigo <> 0;
	END IF;
ELSE
	SELECT codigo, nome, modelo, tamanho, categoria, qtdMinima, estoque FROM material
	WHERE codigo = busca;
END IF;

END$$
DELIMITER ;