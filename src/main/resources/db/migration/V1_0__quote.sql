CREATE TABLE quote (
  id        INT UNSIGNED    NOT NULL AUTO_INCREMENT PRIMARY KEY,
  createdBy INT UNSIGNED    NOT NULL,
  createdAt BIGINT UNSIGNED NOT NULL
)
  ROW_FORMAT = COMPRESSED
  /*!KEY_BLOCK_SIZE = 8*/;
