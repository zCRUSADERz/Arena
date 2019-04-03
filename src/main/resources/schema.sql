# noinspection SqlNoDataSourceInspectionForFile

CREATE TABLE users
(
  name     varchar(25) NOT NULL,
  password varchar(30) NOT NULL,
  health   INT         NOT NULL DEFAULT '100',
  damage   INT         NOT NULL DEFAULT '10',
  PRIMARY KEY (name)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;

CREATE TABLE duels
(
  id      INT       NOT NULL AUTO_INCREMENT,
  created timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;

CREATE TABLE users_in_duels
(
  user_name     VARCHAR(25)  NOT NULL,
  duel_id       INT          NOT NULL,
  last_activity timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  health        int(11)      NOT NULL,
  damage        int(11)      NOT NULL,
  PRIMARY KEY (user_name),
  KEY duel_idx (duel_id),
  CONSTRAINT duel_users_in_duels_fk
    FOREIGN KEY (duel_id)
      REFERENCES duels (id)
      ON DELETE CASCADE
      ON UPDATE RESTRICT,
  CONSTRAINT user_users_in_duels_fk
    FOREIGN KEY (user_name)
      REFERENCES users (name)
      ON DELETE CASCADE
      ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;

CREATE TABLE attack_log
(
  attacker_name varchar(25)  NOT NULL,
  time          timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  duel_id       int(11)      NOT NULL,
  target_name   varchar(25)  NOT NULL,
  PRIMARY KEY (attacker_name, time),
  KEY duel_idx (duel_id),
  KEY target_idx (target_name),
  KEY time_idx (time) USING BTREE,
  CONSTRAINT attacker_attack_log_fk
    FOREIGN KEY (attacker_name)
      REFERENCES users_in_duels (user_name)
      ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT duel_attack_log_fk
    FOREIGN KEY (duel_id)
      REFERENCES duels (id)
      ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT target_attack_log_fk
    FOREIGN KEY (target_name)
      REFERENCES users_in_duels (user_name)
      ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8