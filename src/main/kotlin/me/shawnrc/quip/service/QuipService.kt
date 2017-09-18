package me.shawnrc.quip.service

import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import me.shawnrc.quip.data.QuoteManager
import me.shawnrc.quip.data.UserManager
import me.shawnrc.quip.data.dao.MessageDao
import me.shawnrc.quip.data.dao.QuoteDao
import me.shawnrc.quip.service.mapper.QuipExceptionMapper
import org.jdbi.v3.core.Jdbi

fun main(args: Array<String>) {
  QuipService().run(*args)
}

class QuipService : Application<QuipConfiguration>() {
  override fun getName() = "quip"  // cannot be property - overrides interface method

  override fun initialize(bootstrap: Bootstrap<QuipConfiguration>) {
    bootstrap.objectMapper.registerModule(KotlinModule())
    super.initialize(bootstrap)
  }

  override fun run(config: QuipConfiguration, environment: Environment) {
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

    environment.jersey().urlPattern = "/api/*"
    environment.jersey().register(quoteResource)
    environment.jersey().register(QuipExceptionMapper())
  }
}
