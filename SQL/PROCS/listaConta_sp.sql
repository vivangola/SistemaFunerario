DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `listaConta_sp`(IN busca NVARCHAR(14), IN campo INTEGER, IN aux INTEGER)
BEGIN

DECLARE buscaC NVARCHAR(16);
SET buscaC = CONCAT('%',busca,'%');
	
IF aux = 0 THEN

	SELECT a.codigo, a.dataInclusao, a.situacao, a.vencimentoMensalidade, c.nome as planoDesc
	FROM conta a 
			inner join titular b on a.codigo = b.fk_conta
			inner join plano c on c.codigo = a.fk_plano
	WHERE (campo = 1 and a.codigo LIKE buscaC) or (campo = 2 and b.cpf LIKE buscaC) or (campo not in(1,2));

ELSE

	SELECT a.codigo, a.dataInclusao, a.situacao, a.vencimentoMensalidade, c.nome as planoDesc
	FROM conta a 
			inner join titular b on a.codigo = b.fk_conta
			inner join plano c on c.codigo = a.fk_plano
	
END IF;

END$$
DELIMITER ;
