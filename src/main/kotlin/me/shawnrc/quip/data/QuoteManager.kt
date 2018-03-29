package me.shawnrc.quip.data

import me.shawnrc.quip.core.Message
import me.shawnrc.quip.core.Quote
import me.shawnrc.quip.core.QuoteCore
import me.shawnrc.quip.core.QuoteRow
import me.shawnrc.quip.core.exception.ActionForbiddenException
import me.shawnrc.quip.core.exception.NotFoundException
import me.shawnrc.quip.data.dao.QuoteDao
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.SQLIntegrityConstraintViolationException

class QuoteManager(
    private val quoteDao: QuoteDao,
    private val userManager: UserManager) {

  companion object {
    @JvmStatic
    private val LOG: Logger = LoggerFactory.getLogger(QuoteManager::class.java)
  }

  fun create(quoteCore: QuoteCore, createdBy: Int): Quote {
    val newId = quoteDao.createFull(quoteCore, System.currentTimeMillis(), createdBy)
    LOG.info("created new quote $newId")
    LOG.info("created ${quoteCore.messages.size} new messages for quote $newId")
    return getById(newId)
  }

  fun getAll(): List<Quote> {
    return rowsToQuotes(quoteDao.getAll())
  }

  fun getById(id: Int): Quote {
    val shouldBeOneQuote = rowsToQuotes(quoteDao.getById(id))  // FIXME this is an anti-pattern
    if (shouldBeOneQuote.isEmpty()) {                          // replace this function with one
      val error = "could not find quote for id $id"            // that yields a single quote
      LOG.info(error)
      throw NotFoundException(error)
    } else if (shouldBeOneQuote.size > 1) {
      LOG.error("invalid state! more than one quote for id $id!")
      throw SQLIntegrityConstraintViolationException()
    }
    return shouldBeOneQuote.first()
  }

  fun delete(id: Int, userId: Int) {
    val quote = getById(id)
    if (quote.createdBy != userId && !userManager.isAdmin(userId)) {
      val error = "user [$userId] cannot delete quote [$id]"
      LOG.error(error)
      throw ActionForbiddenException(error)
    }
    quoteDao.delete(id)
  }

  // TODO yield single quotes
  private fun rowsToQuotes(quoteRows: List<QuoteRow>): List<Quote> {
    val rowMap = HashMap<Int, Quote>()
    for (row in quoteRows.sortedBy { it.ordinal }) {
      val message = Message(
          id = row.messageId,
          quoteId = row.quoteId,
          source = row.source,
          ordinal = row.ordinal,
          body = row.body)
      if (row.quoteId !in rowMap) {
        rowMap[row.quoteId] = Quote(
            id = row.quoteId,
            createdBy = row.createdBy,
            createdAt = row.createdAt,
            upvotes = row.upvotes,
            downvotes = row.downvotes)
      }
      rowMap.getValue(row.quoteId).messages.add(message)
    }
    return rowMap.values.toList()
  }
}
