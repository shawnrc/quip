package me.shawnrc.quip.data.dao

import me.shawnrc.quip.core.Vote
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface VoteDao {
  @SqlUpdate("""
      INSERT INTO vote (quoteId, userId, direction)
      VALUES (:quoteId, :userId, :direction)
      ON DUPLICATE KEY UPDATE direction = :direction""")
  fun upsert(@Bind("quoteId") quoteId: Int, @Bind("userId") userId: Int, @Bind("direction") direction: Vote.Direction)

  @SqlUpdate("DELETE FROM vote WHERE quoteId = :quoteId AND userId = :userId")
  fun delete(@Bind("quoteId") quoteId: Int, @Bind("userId") userId: Int)
}
