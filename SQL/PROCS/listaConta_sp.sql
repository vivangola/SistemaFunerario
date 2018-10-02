DELIMITER $$
CREATE PROCEDURE `listaConta_sp`(IN busca NVARCHAR(14), IN campo INTEGER, IN aux INTEGER)
BEGIN

DECLARE buscaC NVARCHAR(14);
SET buscaC = CONCAT('%',busca,'%');
	
IF aux = 0 THEN

	IF campo = 1 THEN
		SELECT c.codigo, t.nome as funcionario, c.dataInclusao, CASE c.situacao WHEN 0 THEN 'Ativo' ELSE 'Inativo' END AS situacao, c.vencimentoMensalidade, p.nome AS plano
		FROM conta c INNER JOIN titular t ON t.fk_conta = c.codigo INNER JOIN plano p ON p.codigo = c.fk_plano
		WHERE c.codigo LIKE buscaC and c.codigo <> 0;
	ELSEIF campo = 2 THEN
		SELECT c.codigo, t.nome as funcionario, c.dataInclusao, CASE c.situacao WHEN 0 THEN 'Ativo' ELSE 'Inativo' END AS situacao, c.vencimentoMensalidade, p.nome AS plano
		FROM conta c INNER JOIN titular t ON t.fk_conta = c.codigo INNER JOIN plano p ON p.codigo = c.fk_plano
		WHERE t.nome LIKE buscaC and c.codigo <> 0;
	ELSE
		SELECT c.codigo, t.nome as funcionario, c.dataInclusao, CASE c.situacao WHEN 0 THEN 'Ativo' ELSE 'Inativo' END AS situacao, c.vencimentoMensalidade, p.nome AS plano
		FROM conta c INNER JOIN titular t ON t.fk_conta = c.codigo INNER JOIN plano p ON p.codigo = c.fk_plano
		WHERE c.codigo <> 0;
	END IF;
ELSE
	SELECT c.codigo, c.dataInclusao, c.situacao, c.vencimentoMensalidade,
		p.codigo as codp, p.nome AS plano, p.carencia, p.valorMensalidade, p.qtdDependente,
		t.nome, t.cargo, t.cpf, t.rg, t.telefone, t.sexo, t.estadoCivil, t.dataNascimento, t.endereco, t.bairro, t.cidade, t.estado, t.cep
		FROM conta c INNER JOIN titular t ON t.fk_conta = c.codigo INNER JOIN plano p ON p.codigo = c.fk_plano
		WHERE c.codigo = busca and c.codigo <> 0;
END IF;

END$$
DELIMITER ;



