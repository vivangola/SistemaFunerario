CREATE DEFINER=`root`@`localhost` PROCEDURE `alterarTitular_sp`(IN conta INTEGER, IN nome varchar(100))
BEGIN
	update titular t inner join dependente d on t.fk_conta = d.fk_conta
    set t.cpf = d.cpf, t.rg = d.rg, t.nome = d.nome, t.falecido = d.falecido
    where d.nome = nome and t.fk_conta = conta;
    
    delete d from dependente d where d.fk_conta = conta and d.nome = nome;
END