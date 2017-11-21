package me.shawnrc.quip.core

data class Message(
    val id: Int,
    val source: Int,
    val ordinal: Int,
    val body: String) {
  constructor(id: Int, ordinal: Int, messageCore: MessageCore) : this(
      id = id,
      source = messageCore.source,
      ordinal = ordinal,
      body = messageCore.body)
}
