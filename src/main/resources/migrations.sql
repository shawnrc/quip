--liquibase formatted sql

-- noinspection SqlDialectInspectionForFile

--changeset shawnrc:1 context:job
CREATE TABLE quote (
  id        INT UNSIGNED    NOT NULL AUTO_INCREMENT PRIMARY KEY,
  createdBy INT UNSIGNED    NOT NULL,
  createdAt BIGINT UNSIGNED NOT NULL
)
  ROW_FORMAT = COMPRESSED
  KEY_BLOCK_SIZE = 8;

--changeset shawnrc:1-1 context:job
CREATE TABLE message (
  id      INT UNSIGNED               NOT NULL AUTO_INCREMENT PRIMARY KEY,
  quoteId INT UNSIGNED               NOT NULL,
  body    TEXT
          CHARACTER SET utf8mb4
          COLLATE utf8mb4_unicode_ci NOT NULL,
  source  INT UNSIGNED               NOT NULL,
  ordinal TINYINT UNSIGNED           NOT NULL,
  CONSTRAINT fk_quoteId_message
      FOREIGN KEY (quoteId)
      REFERENCES quote(id)
      ON DELETE CASCADE,
  INDEX idx_quoteId_ordinal (quoteId, ordinal),
  FULLTEXT idx_message_body (body)
)
  ROW_FORMAT = COMPRESSED
  KEY_BLOCK_SIZE = 8;

--changeset shawnrc:1-2 context:job
CREATE TABLE user (
  id     INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  cshUid VARCHAR(255) NOT NULL
)
  ROW_FORMAT = COMPRESSED
  KEY_BLOCK_SIZE = 8;

--changeset shawnrc:1-3 context:job
CREATE TABLE session (
  accessToken CHAR(32)
              CHARACTER SET ascii
              COLLATE ascii_bin   NOT NULL PRIMARY KEY,
  cshUid      VARCHAR(255)        NOT NULL,
  createdAt   BIGINT UNSIGNED     NOT NULL
)
  ROW_FORMAT = COMPRESSED
  KEY_BLOCK_SIZE = 8;

--changeset shawnrc:1-4 context:job
CREATE TABLE vote (
  quoteId   INT UNSIGNED        NOT NULL,
  userId    INT UNSIGNED        NOT NULL,
  direction ENUM('UP', 'DOWN')
            CHARACTER SET ascii
            COLLATE ascii_bin   NOT NULL,
  CONSTRAINT fk_quoteId_vote
      FOREIGN KEY (quoteId)
      REFERENCES quote(id)
      ON DELETE CASCADE,
  CONSTRAINT fk_userId_vote
      FOREIGN KEY (userId)
      REFERENCES user(id),
  PRIMARY KEY (quoteId, userId),
  INDEX idx_quoteId_direction (quoteId, direction)
)
  ROW_FORMAT = COMPRESSED
  KEY_BLOCK_SIZE = 8;
