package me.shawnrc.quip.data.dao

import me.shawnrc.quip.core.Message
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.SqlBatch
import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface MessageDao {
  @SqlBatch("INSERT INTO message (quoteId, source, ordinal, body) VALUES (:quoteId, :source, :ordinal, :body)")
  fun insert(@Bind("quoteId") quoteId: Int, @BindBean messages: List<Message>)

  @SqlUpdate("DELETE FROM message WHERE id = :id")
  fun delete(id: Int)
}
