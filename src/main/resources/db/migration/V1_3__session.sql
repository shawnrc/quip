CREATE TABLE session (
  id          INT UNSIGNED        NOT NULL AUTO_INCREMENT PRIMARY KEY,
  accessToken CHAR(32)
              /*!CHARACTER SET ascii
              COLLATE ascii_bin*/   NOT NULL,
  createdAt   BIGINT UNSIGNED     NOT NULL
)
  ROW_FORMAT = COMPRESSED
/*!  KEY_BLOCK_SIZE = 8 */;  -- FIXME resolve MySQL/H2 issues for testing
