package me.shawnrc.quip.data

import me.shawnrc.quip.core.Vote
import me.shawnrc.quip.data.dao.VoteDao

class VoteManager(private val voteDao: VoteDao) {
  fun upsert(vote: Vote, userId: Int) = voteDao.upsert(
      vote.quoteId,
      userId,
      vote.direction)

  fun delete(quoteId: Int, userId: Int) = voteDao.delete(quoteId, userId)
}
