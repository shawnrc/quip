package me.shawnrc.quip.data.dao

import me.shawnrc.quip.core.MessageEgg
import me.shawnrc.quip.core.QuoteCore
import me.shawnrc.quip.core.QuoteRow
import me.shawnrc.quip.core.Vote
import org.jdbi.v3.sqlobject.CreateSqlObject
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.jdbi.v3.sqlobject.transaction.Transaction

interface QuoteDao {
  private companion object {
    const val SELECT_PREFIX = """
        SELECT quote.id AS quoteId, message.id AS messageId, quote.createdBy,
            message.ordinal, message.source, quote.createdAt, message.body,
            (SELECT COUNT(*) FROM vote WHERE  vote.direction = "UP" AND vote.quoteId = quote.id) AS upvotes,
            (SELECT COUNT(*) FROM vote WHERE vote.direction = "DOWN" AND vote.quoteId = quote.id) AS downvotes
        FROM quote JOIN message ON message.quoteId = quote.id """
  }

  @CreateSqlObject
  fun getMessageDao(): MessageDao

  @CreateSqlObject
  fun getVoteDao(): VoteDao

  @Transaction
  fun createFull(quoteCore: QuoteCore, createdAt: Long, createdBy: Int): Int {
    val newQuoteId = create(createdAt, createdBy)
    val messageEggs = quoteCore.messages.mapIndexed { index, core ->
      MessageEgg(
          quoteId = newQuoteId,
          ordinal = index,
          messageCore = core.copy(body = core.body.trim()))
    }
    getMessageDao().insert(newQuoteId, messageEggs)
    getVoteDao().upsert(newQuoteId, createdBy, Vote.Direction.UP)
    return newQuoteId
  }

  @GetGeneratedKeys
  @SqlUpdate("INSERT INTO quote (createdAt, createdBy) VALUES (:createdAt, :createdBy)")
  fun create(@Bind("createdAt") createdAt: Long, @Bind("createdBy") createdBy: Int): Int

  @SqlQuery("$SELECT_PREFIX ORDER BY message.ordinal")
  fun getAll(): List<QuoteRow>

  @SqlQuery("$SELECT_PREFIX WHERE quote.id = :id ORDER BY message.ordinal")
  fun getById(@Bind("id") id: Int): List<QuoteRow>

  @SqlUpdate("DELETE FROM quote WHERE id = :id")
  fun delete(@Bind("id") id: Int)
}
