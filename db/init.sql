
CREATE DATABASE IF NOT EXISTS blackjack;
USE blackjack;


CREATE TABLE players (
                         id   BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         wins INT          NOT NULL
);



CREATE TABLE games (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       player_name  VARCHAR(100) NOT NULL,
                       player_score INT          NOT NULL,
                       dealer_score INT          NOT NULL,
                       result       VARCHAR(20)  NOT NULL,
                       created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
