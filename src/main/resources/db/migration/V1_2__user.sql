CREATE TABLE user (
  id         INT UNSIGNED        NOT NULL AUTO_INCREMENT PRIMARY KEY,
  foreignUid VARCHAR(255)
             /*!CHARACTER SET ascii
             COLLATE ascii_bin*/
)
  ROW_FORMAT = COMPRESSED
/*!  KEY_BLOCK_SIZE = 8 */;  -- FIXME resolve MySQL/H2 issues for testing
