CREATE DEFINER=`root`@`localhost` PROCEDURE `listaConta_sp`(IN busca NVARCHAR(14), IN campo INTEGER, IN aux INTEGER)
BEGIN

DECLARE buscaC NVARCHAR(14);
SET buscaC = CONCAT('%',busca,'%');

IF aux = 0 THEN

IF campo = 1 THEN
SELECT c.codigo, t.nome as funcionario, DATE_FORMAT(c.dataInclusao, '%d/%m/%Y') as dataInclusao, CASE c.situacao WHEN 0 THEN 'Ativo' WHEN 2 THEN 'Em Débito' ELSE 'Inativo' END AS situacao, c.vencimentoMensalidade, p.nome AS plano
FROM conta c INNER JOIN titular t ON t.fk_conta = c.codigo INNER JOIN plano p ON p.codigo = c.fk_plano
WHERE c.codigo LIKE buscaC and c.codigo <> 0 and c.situacao <> 1;
ELSEIF campo = 2 THEN
SELECT c.codigo, t.nome as funcionario, DATE_FORMAT(c.dataInclusao, '%d/%m/%Y') as dataInclusao, CASE c.situacao WHEN 0 THEN 'Ativo' WHEN 2 THEN 'Em Débito' ELSE 'Inativo' END AS situacao, c.vencimentoMensalidade, p.nome AS plano
FROM conta c INNER JOIN titular t ON t.fk_conta = c.codigo INNER JOIN plano p ON p.codigo = c.fk_plano
WHERE t.nome LIKE buscaC and c.codigo <> 0 and c.situacao <> 1;
ELSE
SELECT c.codigo, t.nome as funcionario, DATE_FORMAT(c.dataInclusao, '%d/%m/%Y') as dataInclusao, CASE c.situacao WHEN 0 THEN 'Ativo' WHEN 2 THEN 'Em Débito' ELSE 'Inativo' END AS situacao, c.vencimentoMensalidade, p.nome AS plano
FROM conta c INNER JOIN titular t ON t.fk_conta = c.codigo INNER JOIN plano p ON p.codigo = c.fk_plano
WHERE c.codigo <> 0 and c.situacao <> 1;
END IF;
ELSE
SELECT c.codigo, DATE_FORMAT(c.dataInclusao, '%d/%m/%Y') as dataInclusao, c.situacao, c.vencimentoMensalidade,
p.codigo as codp, p.nome AS plano, p.carencia, p.valorMensalidade, p.qtdDependente,
t.nome, t.cargo, t.cpf, t.rg, t.telefone, t.sexo, t.estadoCivil, DATE_FORMAT(t.dataNascimento, '%d/%m/%Y') as dataNascimento, t.endereco, t.bairro, t.cidade, t.estado, t.cep
FROM conta c INNER JOIN titular t ON t.fk_conta = c.codigo INNER JOIN plano p ON p.codigo = c.fk_plano
WHERE c.codigo = busca and c.codigo <> 0 and c.situacao <> 1;
END IF;

END