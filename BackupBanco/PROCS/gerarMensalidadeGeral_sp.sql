CREATE DEFINER=`root`@`localhost` PROCEDURE `gerarMensalidadeGeral_sp`()
BEGIN

	create temporary table A as (
		select codigo 
		from conta 
		where situacao <> 1 and codigo <> 0
	);
    
    insert into mensalidade (vencimento, fk_conta)
    select concat(YEAR(NOW()),'-',MONTH(NOW()),'-',vencimentoMensalidade), codigo 
	from conta 
    where codigo in (select codigo from A);
    
    update mensalidade 
		set periodo = (select concat(nome,'/',year(now())) from meses where mes = month(now()))
    where fk_conta in (select codigo from A) 
		and MONTH(NOW())=MONTH(vencimento) and YEAR(NOW()) = YEAR(vencimento) ;
    
    drop table A;
    call atualizaDebito_sp(-1);
	
END


