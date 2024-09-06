package com.paranid5.vocabulary.data

import cats.effect.IO

import doobie.ConnectionIO
import doobie.implicits.toSqlInterpolator
import doobie.util.log.{LogEvent, LogHandler}
import doobie.util.transactor
import doobie.util.transactor.Transactor

import com.paranid5.vocabulary.data.ops.*

import io.github.cdimascio.dotenv.Dotenv

type IOTransactor = transactor.Transactor.Aux[IO, Unit]

object IOTransactor:
  def apply(dotenv: Dotenv): IOTransactor =
    Transactor.fromDriverManager(
      driver     = "com.mysql.cj.jdbc.Driver",
      url        = dotenv `get` MySqlDbUrl,
      user       = dotenv `get` MySqlDbUser,
      password   = dotenv `get` MySqlDbPassword,
      logHandler = Option(printSqlLogHandler),
    )

  def prepareDatabaseWithUser(dotenv: Dotenv): ConnectionIO[Unit] =
    val admin     = dotenv `get` MySqlDbUser

    val createDb = sql"""CREATE DATABASE IF NOT EXISTS Vocabulary""".effect

    val grantPrivileges = sql"""
      GRANT ALL PRIVILEGES ON Vocabulary.*
      TO $admin@'localhost'
      """.effect

    val removeRemoteHosts = sql"""
      DELETE FROM mysql.user
      WHERE User='root' AND Host NOT IN ('localhost', '127.0.0.1', '::1')
      """.effect

    val flushPrivileges = sql"""FLUSH PRIVILEGES""".effect

    for
      _ ← createDb
      _ ← grantPrivileges
      _ ← removeRemoteHosts
      _ ← flushPrivileges
    yield ()

private def printSqlLogHandler: LogHandler[IO] =
  (logEvent: LogEvent) ⇒
    IO pure println(f"${logEvent.sql} // ${logEvent.args}")
