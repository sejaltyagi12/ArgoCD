--alter IT Software to Engineering column in department table.

INSERT INTO `department`(`dept_id`, `dept_name`) VALUES(9,'Management');

INSERT INTO `designation`(`designation_id`,`designation`,`level`,`dept_id`) VALUES(48,'CTO','LVL_0',9),(49,'CEO','LVL_1',9);