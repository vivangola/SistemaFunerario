CREATE DEFINER=`root`@`localhost` PROCEDURE `geraMensalidadeGeral_sp`(IN codConta int)
BEGIN
	
    create temporary table A as (select codigo from conta situacao <> 1);
    
    insert into mensalidade (vencimento, fk_conta)
    select concat(YEAR(NOW()),'-',MONTH(NOW()),'-',vencimentoMensalidade), codigo from conta 
    where codigo in (select codigo from A);
    
    update mensalidade set periodo = (select concat(nome,'/',year(now())) from meses where mes = month(now()))
    where fk_conta in (select codigo from A);
    
    call atualizaDebito_sp();
    
END