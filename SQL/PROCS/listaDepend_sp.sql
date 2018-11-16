CREATE DEFINER=`root`@`localhost` PROCEDURE `listaDepend_sp`(conta INTEGER)
BEGIN


SELECT t.nome, t.cpf, t.rg, t.sexo, DATE_FORMAT(t.dataNascimento, '%d/%m/%Y') as dataNascimento, t.parentesco
FROM conta c INNER JOIN dependente t ON t.fk_conta = c.codigo 
WHERE t.fk_conta = conta;


END