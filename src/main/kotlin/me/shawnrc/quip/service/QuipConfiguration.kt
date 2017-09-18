package me.shawnrc.quip.service

import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration
import io.dropwizard.db.DataSourceFactory


class QuipConfiguration : Configuration() {
  private var dataSource = DataSourceFactory()

  @JsonProperty("database")
  fun setDataSourceFactory(factory: DataSourceFactory) {
    dataSource = factory
  }

  @JsonProperty("database")
  fun getDataSourceFactory() = dataSource
}
