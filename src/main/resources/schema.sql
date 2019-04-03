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
  id INT NOT NULL AUTO_INCREMENT,
  created timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;

CREATE TABLE users_in_duels
(
  user_name     VARCHAR(25)  NOT NULL,
  duel_id       INT          NOT NULL,
  last_activity timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  health        int(11)      NOT NULL,
  PRIMARY KEY (user_name),
  KEY duel_idx (duel_id),
  CONSTRAINT duel
    FOREIGN KEY (duel_id)
      REFERENCES duels (id)
      ON DELETE CASCADE
      ON UPDATE RESTRICT,
  CONSTRAINT user
    FOREIGN KEY (user_name)
      REFERENCES users (name)
      ON DELETE CASCADE
      ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;