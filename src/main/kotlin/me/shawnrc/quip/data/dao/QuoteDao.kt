package me.shawnrc.quip.data.dao

import me.shawnrc.quip.core.QuoteRow
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface QuoteDao {
  companion object {
    const val SELECT_PREFIX = """
        SELECT quote.id, message.id AS messageId, quote.createdBy, message.ordinal, message.source, quote.createdAt,
        message.body
        FROM quote JOIN message ON message.quoteId = quote.id """
  }

  @GetGeneratedKeys
  @SqlUpdate("INSERT INTO quote (createdAt, createdBy) VALUES (:createdAt, :createdBy)")
  fun create(@Bind("createdAt") createdAt: Long, @Bind("createdBy") createdBy: Int): Int

  @SqlQuery("$SELECT_PREFIX ORDER BY message.ordinal")
  fun getAll(): List<QuoteRow>

  @SqlQuery("""
      $SELECT_PREFIX
      WHERE quote.id = :id
      ORDER BY message.ordinal""")
  fun getById(@Bind("id") id: Int): List<QuoteRow>

  @SqlUpdate("DELETE FROM quote WHERE id = :id")
  fun delete(@Bind("id") id: Int)
}
