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
