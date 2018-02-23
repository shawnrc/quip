package me.shawnrc.quip.service

import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.dropwizard.Application
import io.dropwizard.flyway.FlywayBundle
import io.dropwizard.jdbi3.JdbiFactory
import io.dropwizard.jdbi3.bundles.JdbiExceptionsBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import me.shawnrc.quip.data.QuoteManager
import me.shawnrc.quip.data.UserManager
import me.shawnrc.quip.data.VoteManager
import me.shawnrc.quip.data.dao.QuoteDao
import me.shawnrc.quip.data.dao.VoteDao

class QuipService : Application<QuipConfiguration>() {
  companion object {
    const val URL_PATTERN = "/api/*"

    @JvmStatic fun main(args: Array<String>) {
      QuipService().run(*args)
    }
  }

  override fun getName() = "quip"  // cannot be property - overrides java interface method

  override fun initialize(bootstrap: Bootstrap<QuipConfiguration>) {
    bootstrap.addBundle(object : FlywayBundle<QuipConfiguration>() {
      override fun getDataSourceFactory(config: QuipConfiguration) = config.getDataSourceFactory()
    })
    bootstrap.addBundle(JdbiExceptionsBundle())

    bootstrap.objectMapper.registerModule(KotlinModule())
    super.initialize(bootstrap)
  }

  @Throws(ClassNotFoundException::class)
  override fun run(config: QuipConfiguration, environment: Environment) {
    val jdbi = JdbiFactory().build(environment, config.getDataSourceFactory(), "mysql")
    jdbi.installPlugins()

    val quoteDao = jdbi.onDemand(QuoteDao::class.java)  // TODO DAO injection/binding
    val voteDao = jdbi.onDemand(VoteDao::class.java)

    val userManager = UserManager()
    val quoteManager = QuoteManager(quoteDao, userManager)  // TODO dependency injection
    val voteManager = VoteManager(voteDao)

    val userIdProvider = { 1 }  // TODO actually provide user IDs
    val quoteResource = QuoteResource(quoteManager, userIdProvider)
    val voteResource = VoteResource(voteManager, userIdProvider)

    environment.jersey().apply {
      urlPattern = URL_PATTERN
      register(quoteResource)
      register(voteResource)
    }
  }
}
