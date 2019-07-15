SET FOREIGN_KEY_CHECKS = 0; 
SET group_concat_max_len=25000;
SET @tables = NULL;
SELECT GROUP_CONCAT(table_schema, '.', table_name) INTO @tables
  FROM information_schema.tables 
  WHERE table_schema = 'usmotest'; 

SET @tables = CONCAT('DROP TABLE IF EXISTS ', @tables, ';');
PREPARE stmt FROM @tables;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
SET FOREIGN_KEY_CHECKS = 1; 

