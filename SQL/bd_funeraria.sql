-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: 29-Ago-2018 às 01:30
-- Versão do servidor: 10.1.19-MariaDB
-- PHP Version: 5.5.38

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bd_funeraria`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `conta`
--

CREATE TABLE `conta` (
  `codigo` int(11) PRIMARY KEY NOT NULL,
  `dataInclusao` date DEFAULT NULL,
  `situacao` varchar(30) DEFAULT NULL,
  `vencimentoMensalidade` int DEFAULT NULL,
  `fk_plano` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `contrato`
--

CREATE TABLE `contrato` (
  `codigo` int(11) NOT NULL,
  `data` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `dependente`
--

CREATE TABLE `dependente` (
  `cpf` varchar(14) NOT NULL,
  `dataNascimento` datetime DEFAULT NULL,
  `parentesco` varchar(50) DEFAULT NULL,
  `nome` varchar(100) DEFAULT NULL,
  `rg` varchar(12) DEFAULT NULL,
  `sexo` char(1) DEFAULT NULL,
  `fk_conta` int DEFAULT NULL
  
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
INSERT INTO dependente (nome, cpf, rg, sexo, dataNascimento, parentesco, fk_conta) VALUES('312.312.312-31','aaaaaaa','12312313123','M','(a)-os-Es','26/10/1996',1)
-- --------------------------------------------------------

--
-- Estrutura da tabela `emprestimo`
--

CREATE TABLE `emprestimo` (
  `codigo` int(11) NOT NULL,
  `quantidade` int(11) DEFAULT NULL,
  `dataEntrada` datetime DEFAULT NULL,
  `dataDevolucao` datetime DEFAULT NULL
  `fk_conta` int(11) DEFAULT NULL,
  `fk_material` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `fornecedor`
--

CREATE TABLE `fornecedor` (
  `cnpj` varchar(20) PRIMARY KEY NOT NULL,
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

-- --------------------------------------------------------

--
-- Estrutura da tabela `funcionario`
--

CREATE TABLE `funcionario` (
  `cpf` varchar(14) PRIMARY KEY NOT NULL,
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

-- --------------------------------------------------------

--
-- Estrutura da tabela `empresa`
--

CREATE TABLE `empresa` (
  `cnpj` varchar(20) NOT NULL,
  `nome` varchar(100) DEFAULT NULL,
  `endereco` varchar(100) DEFAULT NULL,
  `bairro` varchar(100) DEFAULT NULL,
  `estado` varchar(2) DEFAULT NULL,
  `cidade` varchar(100) DEFAULT NULL,
  `cep` varchar(9) DEFAULT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `raioAtuacao` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `login`
--

CREATE TABLE `acesso` (
  `login` varchar(10) PRIMARY KEY NOT NULL,
  `senha` varchar(10) NOT NULL,
  `tipo` int DEFAULT NULL,
  `ativo` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
 

-- --------------------------------------------------------

--
-- Estrutura da tabela `material`
--

CREATE TABLE `material` (
  `codigo` int(11) PRIMARY KEY NOT NULL,
  `nome` varchar(50) DEFAULT NULL,
  `modelo` varchar(40) DEFAULT NULL,
  `tamanho` float DEFAULT NULL,
  `categoria` int DEFAULT NULL,
  `qtdMinima` int DEFAULT NULL,
  `estoque` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `mensalidade`
--

CREATE TABLE `mensalidade` (
  `numeroPagamento` int(11) NOT NULL,
  `tipoPagamento` int(11) DEFAULT NULL,
  `dataPagamento` datetime DEFAULT NULL,
  `plano` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `obito`
--

CREATE TABLE `obito` (
  `codigo` int PRIMARY KEY NOT NULL,
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
  `fk_cpf` varchar(100) DEFAULT NULL,
  `fk_conta` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `plano`
--

CREATE TABLE `plano` (
  `codigo` int PRIMARY KEY NOT NULL,
  `nome` varchar(50) DEFAULT NULL,
  `carencia` int DEFAULT NULL,
  `qtdDependente` int DEFAULT NULL,
  `valorMensalidade` decimal(18,2) DEFAULT NULL,
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `recibo`
--

CREATE TABLE `recibo` (
  `codigo` int(11) NOT NULL,
  `dataRecibo` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `tipo_pagamento`
--

CREATE TABLE `tipo_pagamento` (
  `cod` int(11) NOT NULL,
  `descricao` varchar(40) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `titular`
-- 

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
  `fk_conta` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

 
