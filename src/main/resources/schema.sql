# noinspection SqlNoDataSourceInspectionForFile

CREATE TABLE users (
  name varchar(25) NOT NULL,
  password varchar(30) NOT NULL,
  health INT NOT NULL DEFAULT '100',
  damage INT NOT NULL DEFAULT '10',
  PRIMARY KEY (name)
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8;

CREATE TABLE users_queue (
  user_name varchar(25) NOT NULL,
  time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (user_name),
  KEY `time_index` (time) USING BTREE,
  CONSTRAINT name
    FOREIGN KEY (user_name)
      REFERENCES users (name)
      ON DELETE CASCADE
      ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO users (name, password) VALUES ('Alexander', 'password');

INSERT INTO users_queue (user_name) VALUES ('Alexander');