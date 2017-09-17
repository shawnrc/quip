package me.shawnrc.quip.service

import me.shawnrc.quip.core.Quote
import me.shawnrc.quip.core.QuoteCore
import me.shawnrc.quip.data.QuoteManager

//@Path("/v1/quotes")
//@Produces(MediaType.APPLICATION_JSON)
class QuoteResource(private val quoteManager: QuoteManager) {
  //@POST
  fun create(quoteCore: QuoteCore): Quote {
    return quoteManager.create(quoteCore, userIdProvider.get())
  }

  //@GET
  fun get(): Map<String, List<Quote>> {
    return mapOf(pair = "quotes" to quoteManager.getAll())
  }

  //@GET
  //@Path("/{id}")
  fun get(quoteId: Int): Quote {
    return quoteManager.getById(quoteId)
  }

  //@DELETE
  //@Path("/{id}")
  fun delete(quoteId: Int) {
    quoteManager.delete(quoteId, userIdProvider.get())
  }

  private object userIdProvider {
    fun get(): Int {
      return 0
    }
  }
}
