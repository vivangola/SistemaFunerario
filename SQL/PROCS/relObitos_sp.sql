CREATE DEFINER=`root`@`localhost` PROCEDURE `relObito_sp`(IN ini date, fim date)
BEGIN
	SELECT a.codigo, a.fk_cpf,  DATE_FORMAT(a.dataObito, '%d/%m/%Y') as dataObito, a.horaObito, a.localObito 
	FROM obito a inner join conta b on b.codigo = a.fk_conta 
    WHERE a.dataEntero between ini and fim;
END