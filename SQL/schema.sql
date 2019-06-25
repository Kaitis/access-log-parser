CREATE DATABASE IF NOT EXISTS parser;

USE parser;

CREATE TABLE IF NOT EXISTS `log_entry` (
  `id` BIGINT unsigned AUTO_INCREMENT,
  `ip` VARCHAR(20) NOT NULL,
  `logDate` TIMESTAMP NOT NULL,
  `request` VARCHAR(20) NOT NULL,
  `status` INT NOT NULL,
  `userAgent` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
);

# I think this would be good to have with real data
#
# create unique index ip_date__index
#     on log_entry (ip, logDate, userAgent);

CREATE TABLE IF NOT EXISTS `blocked_address` (
  `id` BIGINT unsigned AUTO_INCREMENT,
  `ip` VARCHAR(20) NOT NULL,
  `comment` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
);

