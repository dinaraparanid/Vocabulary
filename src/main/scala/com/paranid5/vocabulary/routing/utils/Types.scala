package com.paranid5.vocabulary.routing.utils

import cats.effect.IO
import com.paranid5.vocabulary.di.AppDependencies
import org.http4s.{HttpRoutes, Request, Response}

private[routing] type AppRoutes       = AppDependencies[HttpRoutes[IO]]
private[routing] type AppHttpResponse = AppDependencies[IO[Response[IO]]]
