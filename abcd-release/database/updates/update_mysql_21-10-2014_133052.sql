
ALTER TABLE `abcd`.`diagram_calculation` 
DROP FOREIGN KEY `FK_ase9cpjt54pn8kfritr4xuunu`;
ALTER TABLE `abcd`.`diagram_calculation` 
CHANGE COLUMN `formExpression_ID` `expression_ID` BIGINT(20) NULL DEFAULT NULL ;
ALTER TABLE `abcd`.`diagram_calculation` 
ADD CONSTRAINT `FK_ase9cpjt54pn8kfritr4xuunu`
  FOREIGN KEY (`expression_ID`)
  REFERENCES `abcd`.`expressions_chain` (`ID`);


ALTER TABLE `diagram_child` 
DROP FOREIGN KEY `FK_tbfvclp8nhuopbecdyswgeu8y`;
ALTER TABLE `diagram_child` 
CHANGE COLUMN `childDiagram_ID` `diagram_ID` BIGINT(20) NULL DEFAULT NULL ;
ALTER TABLE `diagram_child` 
ADD CONSTRAINT `FK_tbfvclp8nhuopbecdyswgeu8y`
  FOREIGN KEY (`diagram_ID`)
  REFERENCES `diagram` (`ID`);


ALTER TABLE `abcd`.`diagram_sink` 
DROP FOREIGN KEY `FK_n7cmqg98x8dm2q0lh9rdpqs9q`;
ALTER TABLE `abcd`.`diagram_sink` 
CHANGE COLUMN `formExpression_ID` `expression_ID` BIGINT(20) NULL DEFAULT NULL ;
ALTER TABLE `abcd`.`diagram_sink` 
ADD CONSTRAINT `FK_n7cmqg98x8dm2q0lh9rdpqs9q`
  FOREIGN KEY (`expression_ID`)
  REFERENCES `abcd`.`expressions_chain` (`ID`);


ALTER TABLE `abcd`.`rule` 
DROP FOREIGN KEY `FK_8imcrv981aypwxbu29g0yc1fl`;
ALTER TABLE `abcd`.`rule` 
CHANGE COLUMN `condition_ID` `conditions_ID` BIGINT(20) NULL DEFAULT NULL ;
ALTER TABLE `abcd`.`rule` 
ADD CONSTRAINT `FK_8imcrv981aypwxbu29g0yc1fl`
  FOREIGN KEY (`conditions_ID`)
  REFERENCES `abcd`.`expressions_chain` (`ID`);

