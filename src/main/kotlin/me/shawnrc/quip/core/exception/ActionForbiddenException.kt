package me.shawnrc.quip.core.exception

class ActionForbiddenException(override val message: String?) : HttpStatusException(message) {
  override val statusCode = 403
}
