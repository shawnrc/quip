package me.shawnrc.quip.core.exception

import javax.ws.rs.core.Response

class NotFoundException(message: String) : NonCatastrophicException(message, Response.Status.NOT_FOUND)
