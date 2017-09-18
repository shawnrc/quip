package me.shawnrc.quip.service.mapper

import me.shawnrc.quip.core.exception.NonCatastrophicException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class QuipExceptionMapper : ExceptionMapper<NonCatastrophicException> {
  override fun toResponse(exception: NonCatastrophicException): Response {
    return exception.response
  }
}
