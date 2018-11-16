CREATE DEFINER=`root`@`localhost` PROCEDURE `listaPessoas_sp`(IN codObito INTEGER, IN conta INTEGER)
BEGIN
	
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
    
END