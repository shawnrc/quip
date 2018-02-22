package me.shawnrc.quip.core

data class MessageEgg(
    val source: Int,
    val body: String,
    val ordinal: Int,
    val quoteId: Int) {
  constructor(ordinal: Int, quoteId: Int, messageCore: MessageCore) : this(
      messageCore.source,
      messageCore.body,
      ordinal,
      quoteId)
}
