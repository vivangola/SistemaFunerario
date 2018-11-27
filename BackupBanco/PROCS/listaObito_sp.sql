CREATE DEFINER=`root`@`localhost` PROCEDURE `listaObito_sp`(IN busca NVARCHAR(14), IN campo INTEGER, IN aux INTEGER)
BEGIN

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

END