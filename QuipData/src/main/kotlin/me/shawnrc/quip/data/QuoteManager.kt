package me.shawnrc.quip.data

import me.shawnrc.quip.core.Message
import me.shawnrc.quip.core.Quote
import me.shawnrc.quip.core.QuoteCore
import me.shawnrc.quip.core.QuoteRow
import me.shawnrc.quip.core.exception.ActionForbiddenException
import me.shawnrc.quip.core.exception.NotFoundException
import me.shawnrc.quip.data.dao.MessageDao
import me.shawnrc.quip.data.dao.QuoteDao
import java.sql.SQLIntegrityConstraintViolationException

class QuoteManager(
    private val messageDao: MessageDao,
    private val quoteDao: QuoteDao,
    private val userManager: UserManager) {

  fun create(quoteCore: QuoteCore, createdBy: Int): Quote {
    val newId = quoteDao.create(System.currentTimeMillis(), createdBy)
    val messages = quoteCore.messages.mapIndexed {
      index, core -> Message(
        id = newId,
        ordinal = index,
        messageCore = core)
    }
    messageDao.insert(newId, messages)
    return getById(newId)
  }

  fun getAll(): List<Quote> {
    return rowsToQuotes(quoteDao.getAll())
  }

  fun getById(id: Int): Quote {
    val shouldBeOneQuote = rowsToQuotes(quoteDao.getById(id))
    if (shouldBeOneQuote.isEmpty()) {
      throw NotFoundException("could not find quote for id [$id]")
    } else if (shouldBeOneQuote.size > 1) {
      throw SQLIntegrityConstraintViolationException("invalid state! more than one quote for id [$id]!")
    }
    return shouldBeOneQuote.first()
  }

  fun delete(id: Int, userId: Int) {
    val quote = getById(id)
    if (quote.createdBy != userId && !userManager.isAdmin(userId)) {
      throw ActionForbiddenException("user [$userId] cannot delete quote [$id]")
    }
    quoteDao.delete(id)
  }

  private fun rowsToQuotes(quoteRows: List<QuoteRow>): List<Quote> {
    val rowMap = HashMap<Int, Quote>()
    for (row in quoteRows.sortedBy(QuoteRow::ordinal)) {
      val message = Message(
          id = row.messageId,
          source = row.source,
          ordinal = row.ordinal,
          body = row.body)
      if (row.id !in rowMap) {
        rowMap.put(row.id, Quote(
            id = row.id,
            createdBy = row.createdBy,
            createdAt = row.createdAt))
      }
      rowMap.getValue(row.id).messages.add(message)
    }
    return rowMap.values.map { it }
  }
}
