package org.ssau.sandbox.configuration;

import java.sql.Connection;
import java.sql.DriverManager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;

/**
 * DatabaseInitializer
 */
@Slf4j
@Component
@Order(1)
public class DatabaseInitializer implements ApplicationRunner {

  @Value("${liquibase.url}")
  String url;
  @Value("${spring.r2dbc.username}")
  String username;
  @Value("${spring.r2dbc.password}")
  String password;
  @Value("${liquibase.change-log}")
  String changeLogFilePath;

  @Override
  public void run(ApplicationArguments args) throws Exception {

    log.info("Выполнение миграций БД");
    log.info("Подключение к БД по адресу: {}", url);

    try (Connection connection = DriverManager.getConnection(url, username,
        password)) {
      Database database = DatabaseFactory.getInstance()
          .findCorrectDatabaseImplementation(new JdbcConnection(connection));
      Liquibase liquibase = new Liquibase(
          changeLogFilePath,
          new ClassLoaderResourceAccessor(),
          database);
      // Запуск миграции
      liquibase.update(""); // Пустая строка означает все контексты
      liquibase.close();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    log.info("Миграция успешно выполнена");
  }

}
