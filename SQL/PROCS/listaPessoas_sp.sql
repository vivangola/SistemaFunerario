CREATE DEFINER=`root`@`localhost` PROCEDURE `listaPessoas_sp`(IN codObito INTEGER, IN conta INTEGER)
BEGIN
	
IF codObito = 0 THEN
    CREATE TEMPORARY TABLE A AS (
    SELECT cpf,concat(nome,' - Titular') as nome FROM titular WHERE fk_conta = conta and falecido = 0);
    INSERT INTO A 
    SELECT cpf,concat(nome,' - ',parentesco) FROM dependente WHERE fk_conta = conta and falecido = 0;
    
    SELECT * FROM A;
ELSE
	CREATE TEMPORARY TABLE A AS (
	SELECT a.cpf,concat(a.nome,' - ',a.parentesco) as nome FROM dependente a INNER JOIN obito b ON a.nome = b.fk_cpf
    WHERE b.codigo = codObito);
    
    SELECT * FROM A;
END IF;
    
END