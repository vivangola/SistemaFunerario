CREATE DEFINER=`root`@`localhost` PROCEDURE `atualizaDebito_sp`(IN cod_conta INTEGER)
BEGIN

	/*
		Situação:
		0 - inativo
		1 - inativo
		2 - em debio
	*/
	
	-- mensalidades vencidas sem pagamento
	create temporary table B as (
		select distinct c.codigo as conta 
		from conta c 
			left join mensalidade a on a.fk_conta = c.codigo 
		where a.vencimento < now() and a.dataPagamento is null and situacao <> 1
	);
	
	-- em debito
	update conta 
		set situacao = 2 
	where codigo in (select conta from B) and situacao <> 1
		and (codigo = cod_conta or cod_conta = -1);
    
	-- ativa
	update conta 
		set situacao = 0 
	where codigo not in (select conta from B) and situacao <> 1
		and (codigo = cod_conta or cod_conta = -1);
	    
    drop table B;
    
END