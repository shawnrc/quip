package me.shawnrc.quip.core.exception

import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

abstract class NonCatastrophicException(
    message: String,
    status: Response.Status) : WebApplicationException(Response.status(status)
        .entity(mapOf(
            "message" to message,
            "code" to status.statusCode))
        .type(MediaType.APPLICATION_JSON_TYPE)
        .build())
