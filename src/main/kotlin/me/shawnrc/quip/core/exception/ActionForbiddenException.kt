package me.shawnrc.quip.core.exception

import javax.ws.rs.core.Response

class ActionForbiddenException(message: String) : RequestEndingException(message, Response.Status.FORBIDDEN)
