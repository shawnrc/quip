package me.shawnrc.quip.core.exception

abstract class HttpStatusException(
    override val message: String?) : Exception(message) {
  abstract val statusCode: Int
}
