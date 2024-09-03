package com.paranid5.vocabulary

import cats.data.{Kleisli, Reader}
import cats.effect.{ExitCode, IO, IOApp}
import com.comcast.ip4s.{ipv4, port}
import com.paranid5.vocabulary.di.{AppDependencies, AppModule}
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
      // TODO: repostiory

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
        res ← impl
      yield res

  private def appService: AppDependencies[Kleisli[IO, Request[IO], Response[IO]]] =
    Reader: _ ⇒
      val resources = resourceServiceBuilder[IO](AssetsPath).toRoutes
      Router("/main" -> resources).orNotFound
