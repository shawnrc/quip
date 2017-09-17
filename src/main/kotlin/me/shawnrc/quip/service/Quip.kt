package me.shawnrc.quip.service

import me.shawnrc.quip.core.QuoteCore
import me.shawnrc.quip.core.exception.HttpStatusException
import me.shawnrc.quip.data.QuoteManager
import me.shawnrc.quip.data.UserManager
import me.shawnrc.quip.data.dao.MessageDao
import me.shawnrc.quip.data.dao.QuoteDao
import org.jdbi.v3.core.Jdbi
import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.features.CallLogging
import org.jetbrains.ktor.features.DefaultHeaders
import org.jetbrains.ktor.gson.GsonSupport
import org.jetbrains.ktor.host.embeddedServer
import org.jetbrains.ktor.http.ContentType
import org.jetbrains.ktor.http.HttpStatusCode
import org.jetbrains.ktor.netty.Netty
import org.jetbrains.ktor.request.receive
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.response.respondText
import org.jetbrains.ktor.routing.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @KtorSpecific
 */

fun main(args: Array<String>) {
  val jdbi = Jdbi.create(
      "jdbc:mysql://localhost/quip?serverTimezone=UTC",
      "root",
      "")
  jdbi.installPlugins()
  val quoteDao = jdbi.onDemand(QuoteDao::class.java)
  val messageDao = jdbi.onDemand(MessageDao::class.java)
  val userManager = UserManager()
  val quoteManager = QuoteManager(messageDao, quoteDao, userManager)
  val quoteResource = QuoteResource(quoteManager)

  val server = embeddedServer(Netty, 5000) {
    install(DefaultHeaders)
    install(CallLogging)
    install(GsonSupport) {
      setPrettyPrinting()
    }

    routing {
      route(Quip.API_BASE) {
        route(path="/quotes") {

          post {
            val core = call.receive<QuoteCore>()
            call.respond(quoteResource.create(core))
          }

          get("/{id}") {
            try {
              val id = call.parameters["id"]
              call.respond(when {
                id == null || id.toIntOrNull() == null -> HttpStatusCode.BadRequest
                else -> quoteResource.get(id.toInt())
              })
            } catch (httpException: HttpStatusException) {
              Quip.LOG.error(httpException.message)
              val statusCode = when (httpException.statusCode) {
                404 -> HttpStatusCode.NotFound.copy(description = httpException.message ?: "not found")
                403 -> HttpStatusCode.Forbidden
                else -> HttpStatusCode.InternalServerError
              }
              call.respondText(
                  text = "{\n  \"error\": \"${httpException.message}\",\n  \"status\": ${statusCode.value}\n}",
                  contentType = ContentType.Application.Json,
                  status = statusCode)
            }
          }

          get {
            call.respond(quoteResource.get())
          }

          delete("/{id}") {
            val id = call.parameters["id"]
            if (id == null || id.toIntOrNull() == null) {
              call.respond(HttpStatusCode.BadRequest)
              return@delete
            }
            quoteResource.delete(id.toInt())
            call.respond(HttpStatusCode.NoContent)
          }
        }
      }
    }
  }
  server.start(wait = true)
}

object Quip {
  const val API_BASE = "/api/v1"
  val LOG: Logger = LoggerFactory.getLogger(Quip::class.java)
}
