CREATE DEFINER=`root`@`localhost` PROCEDURE `atualizaDebito_sp`()
BEGIN

	create temporary table A as (
	select distinct c.codigo as conta from conta c inner join mensalidade a on a.fk_conta = c.codigo 
    where a.vencimento < now() and a.dataPagamento is null);
	-- select codigo from conta where codigo in (select conta from A);
	update conta set situacao = 2 where codigo in (select conta from A) and situacao <> 1;
    
	update conta set situacao = 0 where codigo not in (select conta from A) and situacao <> 1;
    
    drop table a;
    
END