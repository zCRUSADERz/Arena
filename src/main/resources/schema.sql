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
  user_name     VARCHAR(25) NOT NULL,
  duel_id       INT         NOT NULL,
  last_activity timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  health        int(11)     NOT NULL,
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

INSERT INTO users (name, password)
VALUES ('Alexander', 'password');
INSERT INTO users (name, password)
VALUES ('Alexander2', 'password');

INSERT INTO duels VALUES ();

INSERT INTO users_in_duels (user_name, duel_id, health)
SELECT name, 1 AS duel_id, health FROM users
WHERE name = 'Alexander';

DELETE FROM users_in_duels;

SELECT d.created, CURRENT_TIMESTAMP() AS now
FROM users_in_duels AS u
  JOIN duels AS d
    ON u.user_name = 'Alexander' AND u.duel_id = d.id;