CREATE DEFINER=`root`@`localhost` PROCEDURE `atualizaDebito2_sp`(in codconta int)
BEGIN

	create temporary table A as (
	select distinct c.codigo as conta from conta c inner join mensalidade a on a.fk_conta = c.codigo 
    where a.vencimento < now() and c.codigo = codconta and a.dataPagamento is null);
	-- select codigo from conta where codigo in (select conta from A);
	update conta set situacao = 2 where codigo in (select conta from A);
    
    create temporary table B as (
	select distinct c.codigo as conta from conta c inner join mensalidade a on a.fk_conta = c.codigo 
    where a.vencimento < now() and c.codigo = codconta and a.dataPagamento is not null);
	update conta set situacao = 0 where codigo in (select conta from B);
    
END