package me.shawnrc.quip.core

data class Vote(
  val quoteId: Int,
  val direction: Vote.Direction
) {
  enum class Direction {
    UP, DOWN
  }
}
