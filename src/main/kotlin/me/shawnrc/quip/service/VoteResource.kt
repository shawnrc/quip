package me.shawnrc.quip.service

import me.shawnrc.quip.core.Vote
import me.shawnrc.quip.data.VoteManager
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/v1/votes")
@Produces(MediaType.APPLICATION_JSON)
class VoteResource(
    private val voteManager: VoteManager,
    private val userIdProvider: () -> Int) {
  @Consumes(MediaType.APPLICATION_JSON)
  @POST
  fun upsert(vote: Vote) = voteManager.upsert(vote, userIdProvider())

  @DELETE
  @Path("/{quoteId}")
  fun delete(@PathParam("quoteId") quoteId: Int) = voteManager.delete(quoteId, userIdProvider())
}
