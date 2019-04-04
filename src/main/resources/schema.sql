CREATE TABLE `users`
(
  `name`     varchar(25) NOT NULL,
  `password` varchar(30) NOT NULL,
  `health`   int(11)     NOT NULL DEFAULT '100',
  `damage`   int(11)     NOT NULL DEFAULT '10',
  `rating` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `active_duels`
(
  `duel_id` int(11)   NOT NULL AUTO_INCREMENT,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`duel_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `active_duelists`
(
  `user_name`     varchar(25)  NOT NULL,
  `duel_id`       int(11)      NOT NULL,
  `last_activity` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `health`        int(11)      NOT NULL,
  `damage`        int(11)      NOT NULL,
  PRIMARY KEY (`user_name`),
  KEY `duel_idx` (`duel_id`),
  CONSTRAINT `active_duelists_duel_id_fk`
    FOREIGN KEY (`duel_id`) REFERENCES `active_duels` (`duel_id`)
      ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `active_duelists_user_name_fk`
    FOREIGN KEY (`user_name`) REFERENCES `users` (`name`)
      ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `attack_log`
(
  `attacker_name` varchar(25)  NOT NULL,
  `time`          timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `duel_id`       int(11)      NOT NULL,
  `target_name`   varchar(25)  NOT NULL,
  `damage` int(11) NOT NULL,
  PRIMARY KEY (`attacker_name`, `time`),
  KEY `duel_idx` (`duel_id`),
  KEY `time_idx` (`time`) USING BTREE,
  KEY `attack_log_attacker_name_fk_idx` (`attacker_name`),
  KEY `attack_log_target_name_fk_idx` (`target_name`),
  CONSTRAINT `attack_log_attacker_name_fk`
    FOREIGN KEY (`attacker_name`)
      REFERENCES `users` (`name`)
      ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `attack_log_duel_id_fk`
    FOREIGN KEY (`duel_id`)
      REFERENCES `active_duels` (`duel_id`)
      ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `attack_log_target_name_fk`
    FOREIGN KEY (`target_name`)
      REFERENCES `users` (`name`)
      ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `duels_history`
(
  `duel_id`  int(11)   NOT NULL,
  `created`  timestamp NOT NULL,
  `finished` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`duel_id`),
  KEY `duels_history_finished_idx` (`finished`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `duelists_history`
(
  `user_name` varchar(25) NOT NULL,
  `duel_id`   int(11)     NOT NULL,
  `health`    int(11)     NOT NULL,
  `damage`    int(11)     NOT NULL,
  PRIMARY KEY (`user_name`, duel_id),
  KEY `duel_idx` (`duel_id`),
  CONSTRAINT `duelists_history_duel_id_fk`
    FOREIGN KEY (`duel_id`) REFERENCES `duels_history` (`duel_id`)
      ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `duelists_history_user_name_fk`
    FOREIGN KEY (`user_name`) REFERENCES `users` (`name`)
      ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `attack_log_history`
(
  `attacker_name` varchar(25)  NOT NULL,
  `time`          timestamp(3) NOT NULL,
  `duel_id`       int(11)      NOT NULL,
  `target_name`   varchar(25)  NOT NULL,
  `damage` int(11) NOT NULL,
  PRIMARY KEY (`attacker_name`, `time`),
  KEY `duel_idx` (`duel_id`),
  KEY `time_idx` (`time`) USING BTREE,
  KEY `attack_log_history_attacker_name_fk_idx` (`attacker_name`),
  KEY `attack_log_history_target_name_fk_idx` (`target_name`),
  CONSTRAINT `attack_log_history_attacker_name_fk`
    FOREIGN KEY (`attacker_name`) REFERENCES `users` (`name`)
      ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `attack_log_history_duel_id_fk`
    FOREIGN KEY (`duel_id`) REFERENCES `duels_history` (`duel_id`)
      ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `attack_log_history_target_name_fk`
    FOREIGN KEY (`target_name`) REFERENCES `users` (`name`)
      ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `final_blow`
(
  `attacker_name` varchar(25) NOT NULL,
  `duel_id`       int(11)     NOT NULL,
  `target_name`   varchar(25) NOT NULL,
  PRIMARY KEY (`duel_id`),
  KEY `duel_idx` (`duel_id`),
  KEY `final_blow_attacker_fk` (`attacker_name`),
  KEY `final_blow2_target_fk` (`target_name`),
  CONSTRAINT `final_blow2_target_fk`
    FOREIGN KEY (`target_name`)
      REFERENCES `users` (`name`)
      ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `final_blow_attacker_fk`
    FOREIGN KEY (`attacker_name`)
      REFERENCES `users` (`name`)
      ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `final_blow_duel_id_fk`
    FOREIGN KEY (`duel_id`)
      REFERENCES `duels_history` (`duel_id`)
      ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO duels_history (duel_id, created)
SELECT duel_id, created
FROM active_duels
WHERE duel_id = 4;

INSERT INTO duelists_history (user_name, duel_id, health, damage)
SELECT user_name, duel_id, health, damage
FROM active_duelists
WHERE duel_id = 4;

INSERT INTO attack_log_history (attacker_name, time, duel_id, target_name)
SELECT attacker_name, time, duel_id, target_name
FROM attack_log
WHERE duel_id = 4;

DELETE FROM active_duels WHERE duel_id = 4;
DELETE FROM active_duelists WHERE duel_id = 4;
DELETE FROM attack_log WHERE duel_id = 4;

INSERT INTO final_blow (attacker_name, duel_id, target_name)
VALUE ('Alexander', 1, 'Alexander2');
