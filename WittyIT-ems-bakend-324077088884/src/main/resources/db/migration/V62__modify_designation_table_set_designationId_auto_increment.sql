set foreign_key_checks=0;

ALTER TABLE `designation` 
MODIFY `designation_id` INT(4) NOT NULL AUTO_INCREMENT;

set foreign_key_checks=1;