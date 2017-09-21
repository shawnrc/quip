--changeset shawnrc:1
CREATE TABLE quote (
  id        INT UNSIGNED    NOT NULL AUTO_INCREMENT PRIMARY KEY,
  createdBy INT UNSIGNED    NOT NULL,
  createdAt BIGINT UNSIGNED NOT NULL
)
  ROW_FORMAT = COMPRESSED
  KEY_BLOCK_SIZE = 8;

--changeset shawnrc:2
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
      REFERENCES quote (id)
      ON DELETE CASCADE,
  INDEX idx_quoteId_ordinal (quoteId, ordinal)
)
  ROW_FORMAT = COMPRESSED
  KEY_BLOCK_SIZE = 8;
