CREATE DEFINER=`root`@`localhost` PROCEDURE `relMaterial_sp`(IN cat int)
BEGIN
	SELECT codigo, nome, modelo, tamanho, qtdMinima, estoque, 
    case categoria WHEN 1 THEN 'Convalescenca' WHEN 2 THEN 'Decoracoes' WHEN 3 THEN 'Religiosos' ELSE 'URNAS' END AS categoria
    FROM material 	WHERE codigo <> 0 and categoria = cat;
END