create table log_info(
ID INT NOT NULL AUTO_INCREMENT,
IP VARCHAR(50),
TIME VARCHAR(50),
CourseID VARCHAR(10),
Status_Code VARCHAR(10),
Referer VARCHAR(100),
PRIMARY KEY ( ID )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
