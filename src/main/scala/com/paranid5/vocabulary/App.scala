package com.paranid5.vocabulary

import cats.data.{Kleisli, Reader}
import cats.effect.{ExitCode, IO, IOApp}
import com.comcast.ip4s.{ipv4, port}
import com.paranid5.vocabulary.data.IOTransactor
import com.paranid5.vocabulary.di.{AppDependencies, AppModule}
import com.paranid5.vocabulary.routing.main.mainRoutes
import doobie.ConnectionIO
import doobie.syntax.all.*
import org.http4s.{Request, Response}
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import org.http4s.server.staticcontent.resourceServiceBuilder

private val AssetsPath = "/com/paranid5/vocabulary/assets"

object App extends IOApp:
  override def run(args: List[String]): IO[ExitCode] =
    AppModule(runServer() run _) map:
      _.fold(fa = _ ⇒ ExitCode.Error, fb = _ ⇒ ExitCode.Success)

  private def runServer(): AppDependencies[IO[ExitCode]] =
    Reader: appModule ⇒
      val wordsRepository = appModule.wordsModule.wordsRepository

      def prepareDb(): ConnectionIO[Unit] =
        IOTransactor.prepareDatabaseWithUser(appModule.dotenv)

      def createTables(): ConnectionIO[Unit] =
        wordsRepository.createTable()

      def impl: IO[ExitCode] =
        EmberServerBuilder
          .default[IO]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(appService run appModule)
          .build
          .use(_ => IO.never)
          .as(ExitCode.Success)

      for
        _   ← prepareDb().transact(appModule.transactor)
        _   ← createTables().transact(appModule.transactor)
        res ← impl
      yield res

  private def appService: AppDependencies[Kleisli[IO, Request[IO], Response[IO]]] =
    for main ← mainRoutes
      yield Router("/main" -> main).orNotFound
