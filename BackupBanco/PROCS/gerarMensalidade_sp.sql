CREATE DEFINER=`root`@`localhost` PROCEDURE `geraMensalidade_sp`(IN codConta int)
BEGIN

    insert into mensalidade (vencimento, fk_conta)
    select concat(YEAR(NOW()),'-',MONTH(NOW()),'-',vencimentoMensalidade), codigo 
	from conta where codigo = codConta;
    
    update mensalidade set 
		periodo = (select concat(nome,'/',year(now())) from meses where mes = month(now()))
    where fk_conta = codConta and MONTH(NOW())=MONTH(vencimento) and YEAR(NOW()) = YEAR(vencimento) ;
    
    
END