package me.shawnrc.quip.service

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/v1/balls")
@Produces(MediaType.APPLICATION_JSON)
class DummyAuthResource {
  @GET
  fun echo() = "Hello World"
}
