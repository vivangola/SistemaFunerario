CREATE DEFINER=`root`@`localhost` PROCEDURE `listaMensalidade_sp`(IN busca NVARCHAR(100), IN campo INTEGER, IN aux INTEGER)
BEGIN

DECLARE buscaC NVARCHAR(100);
SET buscaC = CONCAT('%',busca,'%');

IF aux = 0 THEN

	IF campo = 1 THEN
		select a.numeroPagamento,periodo, DATE_FORMAT(a.vencimento, '%d/%m/%Y') as vencimento,c.codigo as codConta,b.nome as titular,d.nome as plano, d.valorMensalidade
        from mensalidade a 
        inner join titular b on a.fk_conta = b.fk_conta 
        inner join conta c on c.codigo=b.fk_conta 
        inner join plano d on d.codigo = c.fk_plano
		WHERE b.nome LIKE buscaC and a.numeroPagamento <> 0 and a.dataPagamento is null and a.vencimento < now();
    ELSEIF campo = 2 THEN
		select a.numeroPagamento,periodo, DATE_FORMAT(a.vencimento, '%d/%m/%Y') as vencimento,c.codigo as codConta,b.nome as titular,d.nome as plano, d.valorMensalidade
        from mensalidade a 
        inner join titular b on a.fk_conta = b.fk_conta 
        inner join conta c on c.codigo=b.fk_conta 
        inner join plano d on d.codigo = c.fk_plano
		WHERE a.periodo LIKE buscaC and a.numeroPagamento <> 0 and a.dataPagamento is null and a.vencimento < now();
	ELSE
		select a.numeroPagamento,periodo, DATE_FORMAT(a.vencimento, '%d/%m/%Y') as vencimento,c.codigo as codConta,b.nome as titular,d.nome as plano, d.valorMensalidade
        from mensalidade a 
        inner join titular b on a.fk_conta = b.fk_conta 
        inner join conta c on c.codigo=b.fk_conta 
        inner join plano d on d.codigo = c.fk_plano
		WHERE a.numeroPagamento <> 0 and a.dataPagamento is null and a.vencimento < now();
	END IF;
ELSE
		select a.numeroPagamento,periodo, DATE_FORMAT(a.vencimento, '%d/%m/%Y') as vencimento,c.codigo as codConta,b.nome as titular,d.nome as plano, d.valorMensalidade
        from mensalidade a 
        inner join titular b on a.fk_conta = b.fk_conta 
        inner join conta c on c.codigo=b.fk_conta 
        inner join plano d on d.codigo = c.fk_plano
		WHERE a.numeroPagamento = busca;
END IF;

END