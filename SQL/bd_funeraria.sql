-- phpMyAdmin SQL Dump
-- version 4.8.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 19-Nov-2018 às 23:42
-- Versão do servidor: 10.1.31-MariaDB
-- PHP Version: 7.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bd_funeraria`
--
CREATE DATABASE IF NOT EXISTS `bd_funeraria` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `bd_funeraria`;

DELIMITER $$
--
-- Procedures
--
DROP PROCEDURE IF EXISTS `alterarTitular_sp`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `alterarTitular_sp` (IN `conta` INTEGER, IN `nome` VARCHAR(100))  BEGIN
	update titular t inner join dependente d on t.fk_conta = d.fk_conta
    set t.cpf = d.cpf, t.rg = d.rg, t.nome = d.nome, t.falecido = d.falecido
    where d.nome = nome and t.fk_conta = conta;
    
    delete d from dependente d where d.fk_conta = conta and d.nome = nome;
END$$

DROP PROCEDURE IF EXISTS `atualizaDebito_sp`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `atualizaDebito_sp` ()  BEGIN

	create temporary table A as (
	select distinct c.codigo as conta from conta c inner join mensalidade a on a.fk_conta = c.codigo 
    where a.vencimento < now() and a.dataPagamento is null);
	-- select codigo from conta where codigo in (select conta from A);
	update conta set situacao = 2 where codigo in (select conta from A) and situacao <> 1;
    
	update conta set situacao = 0 where codigo not in (select conta from A) and situacao <> 1;
    
    drop table a;
    
END$$

DROP PROCEDURE IF EXISTS `criarAdmin_sp`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `criarAdmin_sp` ()  BEGIN
	INSERT INTO acesso (login, senha, tipo, ativo, fk_cpf) VALUES ('admin','admin',1,1,'123.456.789-10');
    INSERT INTO funcionario (cpf, nome) VALUES ('123.456.789-10','Administrador do Sistema');
END$$

DROP PROCEDURE IF EXISTS `geraMensalidade_sp`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `geraMensalidade_sp` (IN `codConta` INT)  BEGIN

    insert into mensalidade (vencimento, fk_conta)
    select concat(YEAR(NOW()),'-',MONTH(NOW()),'-',vencimentoMensalidade), codigo from conta where codigo = codConta;
    
    update mensalidade set 
		periodo = (select concat(nome,'/',year(now())) from meses where mes = month(now()))
    where fk_conta = codConta;
    
    call atualizaDebito_sp();
    
END$$

DROP PROCEDURE IF EXISTS `gerarMensalidadeGeral_sp`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `gerarMensalidadeGeral_sp` ()  BEGIN
	create temporary table A as (select codigo from conta where situacao <> 1 and codigo <> 0);
    
    insert into mensalidade (vencimento, fk_conta)
    select concat(YEAR(NOW()),'-',MONTH(NOW()),'-',vencimentoMensalidade), codigo from conta 
    where codigo in (select codigo from A);
    
    update mensalidade set periodo = (select concat(nome,'/',year(now())) from meses where mes = month(now()))
    where fk_conta in (select codigo from A);
    
    drop table A;
    call atualizaDebito_sp();
END$$

DROP PROCEDURE IF EXISTS `listaAcesso_sp`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `listaAcesso_sp` (IN `busca` NVARCHAR(14), IN `campo` INTEGER, IN `aux` INTEGER)  BEGIN

DECLARE buscaC NVARCHAR(14);
SET buscaC = CONCAT('%',busca,'%');
	
IF aux = 0 THEN

	IF campo = 1 THEN
		SELECT nome, login, CASE tipo WHEN 1 THEN 'Administrador' ELSE 'Funcionário' END AS tipo, CASE ativo WHEN 1 THEN 'Ativo' ELSE 'Inativo' END AS ativo 
		FROM acesso inner join funcionario on cpf = fk_cpf
		WHERE login LIKE buscaC;
	ELSEIF campo = 2 THEN
		SELECT nome, login, CASE tipo WHEN 1 THEN 'Administrador' ELSE 'Funcionário' END AS tipo, CASE ativo WHEN 1 THEN 'Ativo' ELSE 'Inativo' END AS ativo 
		FROM acesso inner join funcionario on cpf = fk_cpf
		WHERE nome LIKE buscaC;
	ELSE
		SELECT nome, login, CASE tipo WHEN 1 THEN 'Administrador' ELSE 'Funcionário' END AS tipo, CASE ativo WHEN 1 THEN 'Ativo' ELSE 'Inativo' END AS ativo 
		FROM acesso inner join funcionario on cpf = fk_cpf;
	END IF;
ELSE
	SELECT nome, login, senha, tipo, ativo
		FROM acesso inner join funcionario on cpf = fk_cpf
		WHERE login = busca;
END IF;

END$$

DROP PROCEDURE IF EXISTS `listaConta_sp`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `listaConta_sp` (IN `busca` NVARCHAR(14), IN `campo` INTEGER, IN `aux` INTEGER)  BEGIN

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

END$$

DROP PROCEDURE IF EXISTS `listaDepend_sp`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `listaDepend_sp` (`conta` INTEGER)  BEGIN


SELECT t.nome, t.cpf, t.rg, DATE_FORMAT(t.dataNascimento, '%d/%m/%Y') as dataNascimento, t.parentesco
FROM conta c INNER JOIN dependente t ON t.fk_conta = c.codigo 
WHERE t.fk_conta = conta;


END$$

DROP PROCEDURE IF EXISTS `listaEmprestimo_sp`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `listaEmprestimo_sp` (IN `busca` NVARCHAR(100), IN `campo` INTEGER, IN `aux` INTEGER)  BEGIN

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

END$$

DROP PROCEDURE IF EXISTS `listaFornecedor_sp`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `listaFornecedor_sp` (IN `busca` NVARCHAR(100), IN `campo` INTEGER, IN `aux` INTEGER)  BEGIN

DECLARE buscaC NVARCHAR(100);
SET buscaC = CONCAT('%',busca,'%');
	
IF aux = 0 THEN

	IF campo = 1 THEN
		SELECT nome, cnpj, endereco, bairro, cidade, estado, cep, email, telefone, inscricaoEstadual FROM fornecedor
		WHERE cnpj LIKE buscaC;
	ELSEIF campo = 2 THEN
		SELECT nome, cnpj, endereco, bairro, cidade, estado, cep, email, telefone, inscricaoEstadual FROM fornecedor
		WHERE nome LIKE buscaC;
	ELSE
		SELECT nome, cnpj, endereco, bairro, cidade, estado, cep, email, telefone, inscricaoEstadual FROM fornecedor;
	END IF;
ELSE
	SELECT nome, cnpj, endereco, bairro, cidade, estado, cep, email, telefone, inscricaoEstadual FROM fornecedor
	WHERE cnpj = busca;
END IF;

END$$

DROP PROCEDURE IF EXISTS `listaFuncionario_sp`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `listaFuncionario_sp` (IN `busca` NVARCHAR(100), IN `campo` INTEGER, IN `aux` INTEGER)  BEGIN

DECLARE buscaC NVARCHAR(100);
SET buscaC = CONCAT('%',busca,'%');
	
IF aux = 0 THEN

	IF campo = 1 THEN
		SELECT nome, cargo, cpf, rg, telefone, sexo, estadoCivil, DATE_FORMAT(dataNascimento, '%d/%m/%Y') as dataNascimento, endereco, bairro, cidade, estado, cep FROM funcionario 
		WHERE cpf LIKE buscaC and cpf <> '123.456.789-10';
	ELSEIF campo = 2 THEN
		SELECT nome, cargo, cpf, rg, telefone, sexo, estadoCivil, DATE_FORMAT(dataNascimento, '%d/%m/%Y') as dataNascimento, endereco, bairro, cidade, estado, cep  FROM funcionario 
		WHERE nome LIKE buscaC and cpf <> '123.456.789-10';
	ELSE
		SELECT nome, cargo, cpf, rg, telefone, sexo, estadoCivil, DATE_FORMAT(dataNascimento, '%d/%m/%Y') as dataNascimento, endereco, bairro, cidade, estado, cep  FROM funcionario
		WHERE cpf <> '123.456.789-10';
    END IF;
ELSE
	SELECT nome, cargo, cpf, rg, telefone, sexo, estadoCivil, DATE_FORMAT(dataNascimento, '%d/%m/%Y') as dataNascimento, endereco, bairro, cidade, estado, cep  FROM funcionario 
		WHERE cpf = busca;
END IF;

END$$

DROP PROCEDURE IF EXISTS `listaMateriais_sp`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `listaMateriais_sp` (IN `busca` NVARCHAR(100), IN `campo` INTEGER, IN `aux` INTEGER)  BEGIN

DECLARE buscaC NVARCHAR(100);
SET buscaC = CONCAT('%',busca,'%');

IF aux = 0 THEN

IF campo = 1 THEN
SELECT codigo, nome, modelo, tamanho, 
case categoria WHEN 1 THEN 'Convalescenca' WHEN 2 THEN 'Decoracoes' WHEN 3 THEN 'Religiosos' ELSE 'URNAS' END AS categoria, 
qtdMinima, estoque FROM material
WHERE codigo LIKE buscaC and codigo <> 0;
ELSEIF campo = 2 THEN
SELECT codigo, nome, modelo, tamanho, 
case categoria WHEN 1 THEN 'Convalescenca' WHEN 2 THEN 'Decoracoes' WHEN 3 THEN 'Religiosos' ELSE 'Urnas' END AS categoria, 
qtdMinima, estoque FROM material
WHERE nome LIKE buscaC and codigo <> 0;
ELSEIF campo = 3 THEN
SELECT codigo, nome, modelo, tamanho, 
case categoria WHEN 1 THEN 'Convalescenca' WHEN 2 THEN 'Decoracoes' WHEN 3 THEN 'Religiosos' ELSE 'Urnas' END AS categoria, 
qtdMinima, estoque FROM material
WHERE categoria LIKE buscaC and codigo <> 0;
ELSE
SELECT codigo, nome, modelo, tamanho, 
case categoria WHEN 1 THEN 'Convalescenca' WHEN 2 THEN 'Decoracoes' WHEN 3 THEN 'Religiosos' ELSE 'Urnas' END AS categoria, 
qtdMinima, estoque FROM material
WHERE codigo <> 0;
END IF;
ELSE
SELECT codigo, nome, modelo, tamanho, categoria, qtdMinima, estoque FROM material
WHERE codigo = busca;
END IF;

END$$

DROP PROCEDURE IF EXISTS `listaMensalidade_sp`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `listaMensalidade_sp` (IN `busca` NVARCHAR(100), IN `campo` INTEGER, IN `aux` INTEGER)  BEGIN

DECLARE buscaC NVARCHAR(100);
SET buscaC = CONCAT('%',busca,'%');

IF aux = 0 THEN

	IF campo = 1 THEN
		select a.numeroPagamento,periodo, DATE_FORMAT(a.vencimento, '%d/%m/%Y') as vencimento,c.codigo as codConta,b.nome as titular,CASE c.situacao WHEN 0 THEN 'Ativo' WHEN 2 THEN 'Em Débito' ELSE 'Inativo' END AS situacao,d.nome as plano, d.valorMensalidade
        from mensalidade a 
        inner join titular b on a.fk_conta = b.fk_conta 
        inner join conta c on c.codigo=b.fk_conta 
        inner join plano d on d.codigo = c.fk_plano
		WHERE b.nome LIKE buscaC and a.numeroPagamento <> 0 and a.dataPagamento is null and a.vencimento < now() and c.situacao <> 1;
    ELSEIF campo = 2 THEN
		select a.numeroPagamento,periodo, DATE_FORMAT(a.vencimento, '%d/%m/%Y') as vencimento,c.codigo as codConta,b.nome as titular,CASE c.situacao WHEN 0 THEN 'Ativo' WHEN 2 THEN 'Em Débito' ELSE 'Inativo' END AS situacao,d.nome as plano, d.valorMensalidade
        from mensalidade a 
        inner join titular b on a.fk_conta = b.fk_conta 
        inner join conta c on c.codigo=b.fk_conta 
        inner join plano d on d.codigo = c.fk_plano
		WHERE a.periodo LIKE buscaC and a.numeroPagamento <> 0 and a.dataPagamento is null and a.vencimento < now() and c.situacao <> 1;
	ELSE
		select a.numeroPagamento,periodo, DATE_FORMAT(a.vencimento, '%d/%m/%Y') as vencimento,c.codigo as codConta,b.nome as titular,CASE c.situacao WHEN 0 THEN 'Ativo' WHEN 2 THEN 'Em Débito' ELSE 'Inativo' END AS situacao,d.nome as plano, d.valorMensalidade
        from mensalidade a 
        inner join titular b on a.fk_conta = b.fk_conta 
        inner join conta c on c.codigo=b.fk_conta 
        inner join plano d on d.codigo = c.fk_plano
		WHERE a.numeroPagamento <> 0 and a.dataPagamento is null and a.vencimento < now() and c.situacao <> 1;
	END IF;
ELSE
		select a.numeroPagamento,periodo, DATE_FORMAT(a.vencimento, '%d/%m/%Y') as vencimento,c.codigo as codConta,b.nome as titular,d.nome as plano, d.valorMensalidade
        from mensalidade a 
        inner join titular b on a.fk_conta = b.fk_conta 
        inner join conta c on c.codigo=b.fk_conta 
        inner join plano d on d.codigo = c.fk_plano
		WHERE a.numeroPagamento = busca and c.situacao <> 1;
END IF;

END$$

DROP PROCEDURE IF EXISTS `listaObito_sp`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `listaObito_sp` (IN `busca` NVARCHAR(14), IN `campo` INTEGER, IN `aux` INTEGER)  BEGIN

DECLARE buscaC NVARCHAR(14);
SET buscaC = CONCAT('%',busca,'%');
	
IF aux = 0 THEN

	IF campo = 1 THEN
		SELECT a.codigo, a.fk_cpf, DATE_FORMAT(a.dataObito, '%d/%m/%Y') as dataObito, a.horaEntero, a.localEntero 
        FROM obito a inner join conta b on b.codigo = a.fk_conta		
        WHERE a.codigo LIKE buscaC;
	ELSEIF campo = 2 THEN
		SELECT a.codigo, a.fk_cpf,  DATE_FORMAT(a.dataObito, '%d/%m/%Y') as dataObito, a.horaEntero, a.localEntero  
        FROM obito a inner join conta b on b.codigo = a.fk_conta
		WHERE fk_cpf LIKE buscaC;
	ELSE
		SELECT a.codigo, a.fk_cpf,  DATE_FORMAT(a.dataObito, '%d/%m/%Y') as dataObito, a.horaEntero, a.localEntero 
        FROM obito a inner join conta b on b.codigo = a.fk_conta;
	END IF;
ELSE
	SELECT  b.codigo as fk_conta,c.nome,
    a.codigo,a.horaEntero,a.localEntero,a.horaObito,a.localObito,a.horaVelorio,a.localVelorio,a.fk_cpf,
	DATE_FORMAT(a.dataObito, '%d/%m/%Y') as dataObito,DATE_FORMAT(a.dataEntero, '%d/%m/%Y') as dataEntero,DATE_FORMAT(a.dataVelorio, '%d/%m/%Y') as dataVelorio
        FROM obito a inner join conta b on b.codigo = a.fk_conta inner join titular c on c.fk_conta = b.codigo
		WHERE a.codigo = busca;
END IF;

END$$

DROP PROCEDURE IF EXISTS `listaPessoas_sp`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `listaPessoas_sp` (IN `codObito` INTEGER, IN `conta` INTEGER)  BEGIN
	
IF codObito = 0 THEN
    CREATE TEMPORARY TABLE A AS (
    SELECT cpf,concat(nome,' - Titular') as nome FROM titular WHERE fk_conta = conta and falecido = 0);
    INSERT INTO A 
    SELECT cpf,concat(nome,' - ',parentesco) FROM dependente WHERE fk_conta = conta and falecido = 0;
    
    SELECT * FROM A;
ELSEIF codObito = -1 THEN
	SELECT b.codigo,a.cpf,concat(a.nome,' - ',a.parentesco) as nome FROM dependente a INNER JOIN conta b ON a.fk_conta = b.codigo
    WHERE b.codigo = conta and a.falecido = 0 and YEAR(FROM_DAYS(TO_DAYS(NOW())-TO_DAYS(a.dataNascimento))) >= 18;
ELSE
	CREATE TEMPORARY TABLE A AS (
	SELECT a.cpf,concat(a.nome,' - ',a.parentesco) as nome FROM dependente a INNER JOIN obito b ON a.nome = b.fk_cpf
    WHERE b.codigo = codObito);
    INSERT INTO A 
    SELECT cpf,concat(nome,' - Titular') FROM titular WHERE fk_conta = conta;
    
    SELECT * FROM A;
END IF;
    
END$$

DROP PROCEDURE IF EXISTS `listaPlanos_sp`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `listaPlanos_sp` (IN `busca` NVARCHAR(14), IN `campo` INTEGER, IN `aux` INTEGER)  BEGIN

DECLARE buscaC NVARCHAR(14);
SET buscaC = CONCAT('%',busca,'%');
	
IF aux = 0 THEN

	IF campo = 1 THEN
		SELECT codigo, nome, carencia, valorMensalidade, qtdDependente FROM plano
		WHERE codigo LIKE buscaC and codigo <> 0;
	ELSEIF campo = 2 THEN
		SELECT codigo, nome, carencia, valorMensalidade, qtdDependente FROM plano
		WHERE nome LIKE buscaC and codigo <> 0;
	ELSE
		SELECT codigo, nome, carencia, valorMensalidade, qtdDependente FROM plano 
        WHERE codigo <> 0;
	END IF;
ELSE
	SELECT codigo, nome, carencia, valorMensalidade, qtdDependente FROM plano 
	WHERE codigo = busca and codigo <> 0;
END IF;

END$$

DROP PROCEDURE IF EXISTS `relContas_sp`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `relContas_sp` (IN `situacao` INT)  BEGIN

	SELECT c.codigo, t.nome as funcionario, DATE_FORMAT(c.dataInclusao, '%d/%m/%Y') as dataInclusao, CASE c.situacao WHEN 0 THEN 'Ativo' WHEN 2 THEN 'Em Debito' ELSE 'Inativo' END AS situacao, c.vencimentoMensalidade, p.nome AS plano
	FROM conta c INNER JOIN titular t ON t.fk_conta = c.codigo INNER JOIN plano p ON p.codigo = c.fk_plano
	WHERE c.codigo <> 0 and (c.situacao = situacao or situacao = 3);

END$$

DROP PROCEDURE IF EXISTS `relMaterial_sp`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `relMaterial_sp` (IN `cat` INT)  BEGIN
	SELECT codigo, nome, modelo, tamanho, 
    case categoria WHEN 1 THEN 'Convalescenca' WHEN 2 THEN 'Decoracoes' WHEN 3 THEN 'Religiosos' ELSE 'Urnas' END AS categoria,
    qtdMinima, estoque
    FROM material 	WHERE codigo <> 0 and (categoria = cat or cat = 0);
END$$

DROP PROCEDURE IF EXISTS `relObito_sp`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `relObito_sp` (IN `ini` DATE, `fim` DATE)  BEGIN
	SELECT a.codigo, a.fk_cpf,  DATE_FORMAT(a.dataObito, '%d/%m/%Y') as dataObito, a.horaObito, a.localObito,
								DATE_FORMAT(a.dataObito, '%d/%m/%Y') as dataVelorio, a.horaVelorio, a.localVelorio,
								DATE_FORMAT(a.dataObito, '%d/%m/%Y') as dataEntero, a.horaEntero, a.localEntero 
	FROM obito a inner join conta b on b.codigo = a.fk_conta 
    WHERE a.dataEntero between ini and fim;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estrutura da tabela `acesso`
--

DROP TABLE IF EXISTS `acesso`;
CREATE TABLE `acesso` (
  `login` varchar(10) NOT NULL,
  `senha` varchar(10) NOT NULL,
  `tipo` int(11) DEFAULT NULL,
  `ativo` int(11) NOT NULL,
  `fk_cpf` varchar(14) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `acesso`
--

INSERT INTO `acesso` (`login`, `senha`, `tipo`, `ativo`, `fk_cpf`) VALUES
('admin', 'admin', 1, 1, '123.456.789-10'),
('liro', 'abc123', 2, 1, '868.593.020-29'),
('teste', 'abc123', 1, 2, '111.111.111-11'),
('TESTE123', 'abc123', 1, 1, '123.456.785-89'),
('vivangola', 'abc123', 2, 1, '410.431.028-09');

-- --------------------------------------------------------

--
-- Estrutura da tabela `conta`
--

DROP TABLE IF EXISTS `conta`;
CREATE TABLE `conta` (
  `codigo` int(11) NOT NULL,
  `dataInclusao` date DEFAULT NULL,
  `situacao` varchar(30) DEFAULT NULL,
  `vencimentoMensalidade` int(11) DEFAULT NULL,
  `fk_plano` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `conta`
--

INSERT INTO `conta` (`codigo`, `dataInclusao`, `situacao`, `vencimentoMensalidade`, `fk_plano`) VALUES
(0, '2018-11-19', '0', 0, 0),
(1, '2018-10-08', '2', 10, 2),
(2, '2018-10-09', '0', 10, 2),
(3, '2018-10-08', '1', 5, 2),
(4, '2018-10-09', '2', 10, 3),
(5, '2018-11-15', '2', 5, 1),
(6, '2018-11-19', '0', 20, 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `contrato`
--

DROP TABLE IF EXISTS `contrato`;
CREATE TABLE `contrato` (
  `codigo` int(11) NOT NULL,
  `data` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `dependente`
--

DROP TABLE IF EXISTS `dependente`;
CREATE TABLE `dependente` (
  `cpf` varchar(14) NOT NULL,
  `dataNascimento` datetime DEFAULT NULL,
  `parentesco` varchar(50) DEFAULT NULL,
  `nome` varchar(100) DEFAULT NULL,
  `rg` varchar(12) DEFAULT NULL,
  `sexo` char(1) DEFAULT NULL,
  `fk_conta` int(11) DEFAULT NULL,
  `falecido` int(11) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `dependente`
--

INSERT INTO `dependente` (`cpf`, `dataNascimento`, `parentesco`, `nome`, `rg`, `sexo`, `fk_conta`, `falecido`) VALUES
('123.233.323-23', '1996-10-26 00:00:00', 'Filho(a)', 'Vivangola ', '31231231312', 'M', 4, 0),
('909.349.034-99', '1996-10-26 00:00:00', 'Mãe', 'inFaamous', '00929302302', 'M', 4, 0),
('142.453.621-31', '1996-10-26 00:00:00', 'Pai', 'Outlook4', '31232412414', 'M', 4, 0),
('123.123.123-13', '1996-10-26 00:00:00', 'Esposo(a)', 'teste novo titular', '23123123', 'M', 3, 0),
('111.111.111-11', '2008-10-10 00:00:00', 'Esposo(a)', 'dependente', '3232323232', 'M', 3, 0),
('333.333.333-33', '2008-10-10 00:00:00', 'Mãe', 'TESTE', '1231313', NULL, 5, 0),
('548.784.949-84', '1996-10-26 00:00:00', 'Filho(a)', 'Junilsinho Periz', '547484', NULL, 2, 0),
('012.302.021-23', '2013-01-01 00:00:00', 'Filho(a)', 'teste de', '3333332222', NULL, 1, 0),
('574.875.487-54', '1996-10-26 00:00:00', 'Esposo(a)', 'Leitaozinho veio', '12312312313', NULL, 6, 0),
('654.898.875-54', '2010-10-10 00:00:00', 'Filho(a)', 'Tiaozinho Preto', '35623562356', NULL, 6, 0);

-- --------------------------------------------------------

--
-- Estrutura da tabela `empresa`
--

DROP TABLE IF EXISTS `empresa`;
CREATE TABLE `empresa` (
  `cnpj` varchar(20) NOT NULL,
  `nome` varchar(100) DEFAULT NULL,
  `endereco` varchar(100) DEFAULT NULL,
  `bairro` varchar(50) DEFAULT NULL,
  `estado` varchar(2) DEFAULT NULL,
  `cidade` varchar(50) DEFAULT NULL,
  `cep` varchar(9) DEFAULT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  `raioAtuacao` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `empresa`
--

INSERT INTO `empresa` (`cnpj`, `nome`, `endereco`, `bairro`, `estado`, `cidade`, `cep`, `telefone`, `email`, `raioAtuacao`) VALUES
('00.000.000/0000-00', 'Funeraria Bom Jesus', 'Rua Luiz de Souza Coelho 675', 'Centro', 'SP', 'Ipaussu', '18950-000', '(14)33441896', 'funeraria@bomjesus.com.br', 300);

-- --------------------------------------------------------

--
-- Estrutura da tabela `emprestimo`
--

DROP TABLE IF EXISTS `emprestimo`;
CREATE TABLE `emprestimo` (
  `codigo` int(11) NOT NULL,
  `quantidade` int(11) DEFAULT NULL,
  `dataEntrada` datetime DEFAULT NULL,
  `dataDevolucao` datetime DEFAULT NULL,
  `fk_conta` int(11) DEFAULT NULL,
  `fk_material` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `emprestimo`
--

INSERT INTO `emprestimo` (`codigo`, `quantidade`, `dataEntrada`, `dataDevolucao`, `fk_conta`, `fk_material`) VALUES
(0, NULL, NULL, NULL, NULL, NULL),
(1, 5, '2018-11-01 00:00:00', '2018-11-11 00:00:00', 4, 3),
(2, 3, '2018-10-26 00:00:00', '2018-11-11 00:00:00', 4, 3),
(3, 3, '2018-10-26 00:00:00', '2018-11-11 00:00:00', 4, 3),
(4, 4, '2018-11-11 00:00:00', '2018-11-11 00:00:00', 4, 3),
(5, 4, '2018-11-11 00:00:00', '2018-11-11 00:00:00', 4, 3),
(6, 4, '2018-11-11 00:00:00', '2018-11-11 00:00:00', 4, 3),
(7, 1, '2018-11-15 00:00:00', '2018-11-15 00:00:00', 2, 3),
(8, 2, '2018-11-17 00:00:00', NULL, 5, 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `fornecedor`
--

DROP TABLE IF EXISTS `fornecedor`;
CREATE TABLE `fornecedor` (
  `cnpj` varchar(20) NOT NULL,
  `nome` varchar(100) DEFAULT NULL,
  `estado` varchar(2) DEFAULT NULL,
  `inscricaoEstadual` varchar(40) DEFAULT NULL,
  `endereco` varchar(100) DEFAULT NULL,
  `bairro` varchar(100) DEFAULT NULL,
  `cep` varchar(9) DEFAULT NULL,
  `cidade` varchar(100) DEFAULT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `fornecedor`
--

INSERT INTO `fornecedor` (`cnpj`, `nome`, `estado`, `inscricaoEstadual`, `endereco`, `bairro`, `cep`, `cidade`, `telefone`, `email`) VALUES
('12.312.312/3123-12', 'Fornecedor Teste', 'AL', 'ASDADA', 'fRONEASIODANSD', 'ALTERADA', '11111-111', 'CITY', '(12)31231231', 'EMAIL'),
('01.010.010/1010-10', 'Fornecedor de Kxão', 'SP', '123.321.123', 'Rua do Endereco 123', 'Centro', '18950-000', 'Ipaussu', '(14)33025547', 'fornecedor@kxao.com.br'),
('12.345.648/7987-98', 'Fornecedor do Caixao', 'DF', '15165161651', 'sdasdasdasda', 'teste', '18950-000', 'IpAUSSSU', '(14)33442526', 'jasudasda@asdasd.com');

-- --------------------------------------------------------

--
-- Estrutura da tabela `funcionario`
--

DROP TABLE IF EXISTS `funcionario`;
CREATE TABLE `funcionario` (
  `cpf` varchar(14) NOT NULL,
  `cep` varchar(9) DEFAULT NULL,
  `estado` varchar(2) DEFAULT NULL,
  `cidade` varchar(100) DEFAULT NULL,
  `rg` varchar(12) DEFAULT NULL,
  `nome` varchar(100) NOT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `dataNascimento` date DEFAULT NULL,
  `sexo` char(1) DEFAULT NULL,
  `estadoCivil` varchar(15) DEFAULT NULL,
  `cargo` varchar(30) DEFAULT NULL,
  `endereco` varchar(100) DEFAULT NULL,
  `bairro` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `funcionario`
--

INSERT INTO `funcionario` (`cpf`, `cep`, `estado`, `cidade`, `rg`, `nome`, `telefone`, `dataNascimento`, `sexo`, `estadoCivil`, `cargo`, `endereco`, `bairro`) VALUES
('111.111.111-11', '00000-000', 'AL', 'asdasdasd', '11', 'Vivangola da Silva', '(11)111111111', '1996-10-26', 'F', 'Casado(a)', 'Cobrador(a)', 'asdasdasd', 'asdads'),
('123.456.785-89', '18950-000', 'BA', 'ISUDHQSIDUHQISDUH', '11652515151', 'Elaine Pasqualini', '(14)997828864', '1996-10-26', 'F', 'Divorciado(a)', 'Agente Funebre', 'DAUSHDIUQSIDUHQIHDSQ', 'DIQUSHDIQUSDHQIDH'),
('123.456.789-10', '18950-000', 'SP', 'Cidade do Administrador do Sistema', '533515774', 'Administrador do Sistema', '(14)997828864', '1996-10-26', 'M', 'Casado(a)', 'Administrador(a)', 'Rua do Administrador do Sistema', 'Bairro do Administrador do Sistema'),
('410.431.028-09', '18950-000', 'SP', 'Ipaussu', '533515774', 'Denilson Perez Junior', '(14)997828864', '1996-10-26', 'M', 'Solteiro(a)', 'Administrador(a)', 'Rua Professor Antonio Martins', 'Vila Melgis'),
('868.593.020-29', '18950-000', 'AP', 'Ipaussu', '19283729', 'Dirceu Dias ', '(14)997825577', '1956-10-10', 'M', 'Casado(a)', 'Cobrador(a)', 'Rua Luiz de Souza Coelho', 'Brasil Novo');

-- --------------------------------------------------------

--
-- Estrutura da tabela `material`
--

DROP TABLE IF EXISTS `material`;
CREATE TABLE `material` (
  `codigo` int(11) NOT NULL,
  `nome` varchar(50) DEFAULT NULL,
  `modelo` varchar(40) DEFAULT NULL,
  `tamanho` varchar(50) DEFAULT NULL,
  `categoria` int(11) DEFAULT NULL,
  `qtdMinima` int(11) DEFAULT NULL,
  `estoque` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `material`
--

INSERT INTO `material` (`codigo`, `nome`, `modelo`, `tamanho`, `categoria`, `qtdMinima`, `estoque`) VALUES
(0, NULL, NULL, NULL, NULL, NULL, NULL),
(1, 'Cadeira de Rodas', 'Infantil', '10.5', 1, 10, 25),
(2, 'Cadeira de Banho do Robson', 'Gold', '12.5', 1, 5, 10),
(3, 'Teste Emprestimo 10', 'Bonito', '10', 1, 5, 4),
(4, 'Urna Jamaicana', '250XS', '150', 4, 5, 0),
(5, 'Crussifixo de Jesus', 'SS100', '30', 3, 10, 0),
(6, 'Suporte para Biblia', 'CC', '20', 2, 2, 0),
(7, 'Biblia', 'Catolico', 'Medio', 3, 5, 0);

-- --------------------------------------------------------

--
-- Estrutura da tabela `mensalidade`
--

DROP TABLE IF EXISTS `mensalidade`;
CREATE TABLE `mensalidade` (
  `numeroPagamento` int(11) NOT NULL,
  `tipoPagamento` int(11) DEFAULT NULL,
  `vencimento` date DEFAULT NULL,
  `periodo` varchar(50) DEFAULT NULL,
  `dataPagamento` datetime DEFAULT NULL,
  `fk_conta` int(11) DEFAULT NULL,
  `valor` decimal(12,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `mensalidade`
--

INSERT INTO `mensalidade` (`numeroPagamento`, `tipoPagamento`, `vencimento`, `periodo`, `dataPagamento`, `fk_conta`, `valor`) VALUES
(39, NULL, '2018-11-10', 'NOVEMBRO/2018', NULL, 1, NULL),
(40, 2, '2018-11-10', 'NOVEMBRO/2018', '2018-11-18 00:00:00', 2, '25.00'),
(41, NULL, '2018-11-10', 'NOVEMBRO/2018', NULL, 4, NULL),
(42, NULL, '2018-11-05', 'NOVEMBRO/2018', NULL, 5, NULL),
(43, NULL, '2018-11-20', 'NOVEMBRO/2018', NULL, 6, NULL);

-- --------------------------------------------------------

--
-- Estrutura da tabela `meses`
--

DROP TABLE IF EXISTS `meses`;
CREATE TABLE `meses` (
  `mes` int(11) NOT NULL,
  `nome` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `meses`
--

INSERT INTO `meses` (`mes`, `nome`) VALUES
(1, 'JANEIRO'),
(2, 'FEVEREIRO'),
(3, 'MARÇO'),
(4, 'ABRIL'),
(5, 'MAIO'),
(6, 'JUNHO'),
(7, 'JULHO'),
(8, 'AGOSTO'),
(9, 'SETEMBRO'),
(10, 'OUTUBRO'),
(11, 'NOVEMBRO'),
(12, 'DEZEMBRO'),
(13, 'teste');

-- --------------------------------------------------------

--
-- Estrutura da tabela `obito`
--

DROP TABLE IF EXISTS `obito`;
CREATE TABLE `obito` (
  `codigo` int(11) NOT NULL,
  `localVelorio` varchar(100) DEFAULT NULL,
  `dataVelorio` date DEFAULT NULL,
  `horaVelorio` varchar(50) DEFAULT NULL,
  `localObito` varchar(100) DEFAULT NULL,
  `dataObito` date DEFAULT NULL,
  `horaObito` varchar(50) DEFAULT NULL,
  `localEntero` varchar(100) DEFAULT NULL,
  `dataEntero` date DEFAULT NULL,
  `horaEntero` varchar(50) DEFAULT NULL,
  `causa` varchar(100) DEFAULT NULL,
  `fk_conta` int(11) DEFAULT NULL,
  `fk_cpf` varchar(300) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `obito`
--

INSERT INTO `obito` (`codigo`, `localVelorio`, `dataVelorio`, `horaVelorio`, `localObito`, `dataObito`, `horaObito`, `localEntero`, `dataEntero`, `horaEntero`, `causa`, `fk_conta`, `fk_cpf`) VALUES
(0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(1, 'ASDASD12', '2018-11-15', '12:12', 'SDASDASD', '2018-11-15', '12:12', 'ASDASD', '2018-11-15', '13:12', NULL, 3, 'obito testar inativar '),
(2, 'teste1', '2018-11-19', '12:45', 'teste', '2018-11-19', '10:45', 'teste1', '2018-11-19', '10:10', NULL, 6, 'Gabriel da Silva Grimes ');

-- --------------------------------------------------------

--
-- Estrutura da tabela `plano`
--

DROP TABLE IF EXISTS `plano`;
CREATE TABLE `plano` (
  `codigo` int(11) NOT NULL,
  `nome` varchar(50) DEFAULT NULL,
  `carencia` int(11) DEFAULT NULL,
  `qtdDependente` int(11) DEFAULT NULL,
  `valorMensalidade` decimal(18,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `plano`
--

INSERT INTO `plano` (`codigo`, `nome`, `carencia`, `qtdDependente`, `valorMensalidade`) VALUES
(0, NULL, NULL, NULL, NULL),
(1, 'GOLD', 3, 10, '20.00'),
(2, 'BRONZE', 3, 2, '25.00'),
(3, 'Diamante', 3, 3, '25.90');

-- --------------------------------------------------------

--
-- Estrutura da tabela `recibo`
--

DROP TABLE IF EXISTS `recibo`;
CREATE TABLE `recibo` (
  `codigo` int(11) NOT NULL,
  `dataRecibo` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `tipo_pagamento`
--

DROP TABLE IF EXISTS `tipo_pagamento`;
CREATE TABLE `tipo_pagamento` (
  `cod` int(11) NOT NULL,
  `descricao` varchar(40) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `titular`
--

DROP TABLE IF EXISTS `titular`;
CREATE TABLE `titular` (
  `cpf` varchar(14) NOT NULL,
  `cep` varchar(9) DEFAULT NULL,
  `estado` varchar(2) DEFAULT NULL,
  `cidade` varchar(100) DEFAULT NULL,
  `rg` varchar(12) DEFAULT NULL,
  `nome` varchar(100) NOT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `dataNascimento` date DEFAULT NULL,
  `sexo` char(1) DEFAULT NULL,
  `estadoCivil` varchar(15) DEFAULT NULL,
  `cargo` varchar(30) DEFAULT NULL,
  `endereco` varchar(100) DEFAULT NULL,
  `bairro` varchar(100) DEFAULT NULL,
  `fk_conta` int(11) DEFAULT NULL,
  `falecido` int(11) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `titular`
--

INSERT INTO `titular` (`cpf`, `cep`, `estado`, `cidade`, `rg`, `nome`, `telefone`, `dataNascimento`, `sexo`, `estadoCivil`, `cargo`, `endereco`, `bairro`, `fk_conta`, `falecido`) VALUES
('020.245.874-00', '18950-000', 'BA', 'VIVANGOLA', '15615616516', 'Vivangola Mix', '(14)997828864', '1996-10-26', 'F', 'Divorciado(a)', ' xqdqsdqdqsd', 'VIVANGOLA', 'VIVANGOLA', 1, 0),
('000.000.000-00', '18950-000', 'AL', 'DQSDQSDQ', '12133131133', 'Teste Obito Inativa', '(14)997828864', '1996-10-26', 'F', 'Divorciado(a)', ' SDQDQSQD', 'TESTE3333', 'DQSDQSDQSD', 2, 0),
('123.123.123-13', '18950-000', 'AL', 'DQDWDQ', '23123123', 'obito testar inativar', '(21)499782864', '1996-01-26', 'F', 'Casado(a)', ' SQDQDQSD', 'ASDADA', 'DQDQWDQ', 3, 0),
('987.858.785-85', '18950-000', 'GO', 'Cidade Teste', '19849849849', 'Junilson Perez Junior', '(14)997828664', '1996-10-26', 'F', 'Solteiro(a)', ' Programador', 'Rua da Teste', 'Bairro Teste', 4, 0),
('123.123.121-41', '18950-000', 'AL', 'Mariana Matos', '12312312312', 'Mariana Matos', '(22)222222222', '1996-10-26', 'F', 'Casado(a)', ' Mariana Matos', 'Mariana Matos', 'Mariana Matos', 5, 0),
('385.736.192-73', '18950-000', 'AL', 'Cidade do Grimes', '82748727382', 'Gabriel da Silva Grimes', '(14)334425787', '1996-10-26', 'F', 'Divorciado(a)', ' Programador', 'Rua do Grimes ', 'Bairro do Grimes', 6, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `acesso`
--
ALTER TABLE `acesso`
  ADD PRIMARY KEY (`login`);

--
-- Indexes for table `conta`
--
ALTER TABLE `conta`
  ADD PRIMARY KEY (`codigo`);

--
-- Indexes for table `contrato`
--
ALTER TABLE `contrato`
  ADD PRIMARY KEY (`codigo`);

--
-- Indexes for table `funcionario`
--
ALTER TABLE `funcionario`
  ADD PRIMARY KEY (`cpf`);

--
-- Indexes for table `material`
--
ALTER TABLE `material`
  ADD PRIMARY KEY (`codigo`);

--
-- Indexes for table `mensalidade`
--
ALTER TABLE `mensalidade`
  ADD PRIMARY KEY (`numeroPagamento`);

--
-- Indexes for table `meses`
--
ALTER TABLE `meses`
  ADD PRIMARY KEY (`mes`);

--
-- Indexes for table `obito`
--
ALTER TABLE `obito`
  ADD PRIMARY KEY (`codigo`);

--
-- Indexes for table `plano`
--
ALTER TABLE `plano`
  ADD PRIMARY KEY (`codigo`);

--
-- Indexes for table `recibo`
--
ALTER TABLE `recibo`
  ADD PRIMARY KEY (`codigo`);

--
-- Indexes for table `tipo_pagamento`
--
ALTER TABLE `tipo_pagamento`
  ADD PRIMARY KEY (`cod`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `mensalidade`
--
ALTER TABLE `mensalidade`
  MODIFY `numeroPagamento` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;

--
-- AUTO_INCREMENT for table `recibo`
--
ALTER TABLE `recibo`
  MODIFY `codigo` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tipo_pagamento`
--
ALTER TABLE `tipo_pagamento`
  MODIFY `cod` int(11) NOT NULL AUTO_INCREMENT;

DELIMITER $$
--
-- Eventos
--
DROP EVENT `gerarMensalidadeGerar_evt`$$
CREATE DEFINER=`root`@`localhost` EVENT `gerarMensalidadeGerar_evt` ON SCHEDULE EVERY 1 MONTH STARTS '2018-12-01 00:00:00' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
 call gerarMensalidadeGeral_sp();
END$$

DELIMITER ;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
