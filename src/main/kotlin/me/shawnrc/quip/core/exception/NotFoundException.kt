package me.shawnrc.quip.core.exception

import javax.ws.rs.core.Response

class NotFoundException(message: String) : RequestEndingException(message, Response.Status.NOT_FOUND)
