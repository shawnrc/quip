package me.shawnrc.quip.core.exception

import javax.ws.rs.core.Response

class ActionForbiddenException(message: String) : NonCatastrophicException(message, Response.Status.FORBIDDEN)
