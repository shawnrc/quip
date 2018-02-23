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
