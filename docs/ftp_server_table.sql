CREATE TABLE `ftp_user` (
      `userid`            VARCHAR(64) NOT NULL COLLATE 'utf8mb3_general_ci',
      `userpassword`     VARCHAR(64) NULL DEFAULT NULL COLLATE 'utf8mb3_general_ci',
      `homedirectory`    VARCHAR(128) NOT NULL COLLATE 'utf8mb3_general_ci',
      `enableflag`        TINYINT(1) NULL DEFAULT '1',
      `writepermission`   TINYINT(1) NULL DEFAULT '0',
      `idletime`           INT(11) NULL DEFAULT '0',
      `uploadrate`        INT(11) NULL DEFAULT '0',
      `downloadrate`      INT(11) NULL DEFAULT '0',
      `maxloginnumber`   INT(11) NULL DEFAULT '0',
      `maxloginperip`     INT(11) NULL DEFAULT '0',
      PRIMARY KEY (`userid`) USING BTREE
)
    COLLATE='utf8mb3_general_ci'
ENGINE=InnoDB
;