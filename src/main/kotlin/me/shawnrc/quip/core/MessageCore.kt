package me.shawnrc.quip.core

data class MessageCore(
    val source: Int,
    val body: String) {
  init {
    require(source > 0) { "invalid source id" }
    require(body.length in MESSAGE_SIZE_RANGE) {
      "invalid size for message body (${body.length})"
    }
  }

  companion object {
    private val MESSAGE_SIZE_RANGE = 0..(Short.MAX_VALUE * 2)
  }
}
