CREATE DEFINER=`root`@`localhost` PROCEDURE `listaEmprestimo_sp`(IN busca NVARCHAR(100), IN campo INTEGER, IN aux INTEGER)
BEGIN

DECLARE buscaC NVARCHAR(100);
SET buscaC = CONCAT('%',busca,'%');

IF aux = 0 THEN

	IF campo = 1 THEN
		SELECT a.codigo,c.nome as material,a.quantidade, DATE_FORMAT(a.dataEntrada, '%d/%m/%Y') as dataEntrada,b.nome as titular FROM emprestimo a INNER JOIN titular b ON a.fk_conta = b.fk_conta INNER JOIN material c ON c.codigo=a.fk_material
		WHERE b.nome LIKE buscaC and a.codigo <> 0 and a.dataDevolucao is null;
    ELSEIF campo = 2 THEN
		SELECT a.codigo,c.nome as material,a.quantidade,DATE_FORMAT(a.dataEntrada, '%d/%m/%Y') as dataEntrada,b.nome as titular  FROM emprestimo a INNER JOIN titular b ON a.fk_conta = b.fk_conta INNER JOIN material c ON c.codigo=a.fk_material
        WHERE c.nome LIKE buscaC and a.codigo <> 0 and a.dataDevolucao is null;
	ELSE
		SELECT a.codigo,c.nome as material,a.quantidade,DATE_FORMAT(a.dataEntrada, '%d/%m/%Y') as dataEntrada,b.nome as titular  FROM emprestimo a INNER JOIN titular b ON a.fk_conta = b.fk_conta INNER JOIN material c ON c.codigo=a.fk_material
        WHERE a.codigo <> 0 and a.dataDevolucao is null;
	END IF;
ELSE
	SELECT a.codigo,a.quantidade,DATE_FORMAT(a.dataEntrada, '%d/%m/%Y') as dataEntrada,b.fk_conta,b.nome,c.codigo as codMaterial, c.nome as material, c.modelo, c.tamanho, c.estoque, c.categoria FROM emprestimo a INNER JOIN titular b ON b.fk_conta = a.fk_conta INNER JOIN material c ON c.codigo = a.fk_material
	WHERE a.codigo = busca;
END IF;

END