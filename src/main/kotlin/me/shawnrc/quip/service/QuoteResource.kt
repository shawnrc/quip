package me.shawnrc.quip.service

import me.shawnrc.quip.core.Quote
import me.shawnrc.quip.core.QuoteCore
import me.shawnrc.quip.data.QuoteManager
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/v1/quotes")
@Produces(MediaType.APPLICATION_JSON)
class QuoteResource(
    private val quoteManager: QuoteManager,
    private val userIdProvider: () -> Int) {
  @Consumes(MediaType.APPLICATION_JSON)
  @POST
  fun create(quoteCore: QuoteCore): Quote = quoteManager.create(quoteCore, userIdProvider())

  @GET
  fun get(): Map<String, List<Quote>> = mapOf("quotes" to quoteManager.getAll())

  @GET
  @Path("/{id}")
  fun get(@PathParam("id") quoteId: Int): Quote = quoteManager.getById(quoteId)

  @DELETE
  @Path("/{id}")
  fun delete(@PathParam("id") quoteId: Int) {
    quoteManager.delete(quoteId, userIdProvider())
  }
}
