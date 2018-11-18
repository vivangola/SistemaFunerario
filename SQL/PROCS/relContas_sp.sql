CREATE DEFINER=`root`@`localhost` PROCEDURE `relContas_sp`(IN situacao int)
BEGIN

	SELECT c.codigo, t.nome as funcionario, DATE_FORMAT(c.dataInclusao, '%d/%m/%Y') as dataInclusao, CASE c.situacao WHEN 0 THEN 'Ativo' WHEN 2 THEN 'Em Debito' ELSE 'Inativo' END AS situacao, c.vencimentoMensalidade, p.nome AS plano
	FROM conta c INNER JOIN titular t ON t.fk_conta = c.codigo INNER JOIN plano p ON p.codigo = c.fk_plano
	WHERE c.codigo <> 0 and (c.situacao = situacao or situacao = 3);

END