package me.shawnrc.quip.core.exception

class NotFoundException(override val message: String?) : HttpStatusException(message) {
  override val statusCode = 404
}

