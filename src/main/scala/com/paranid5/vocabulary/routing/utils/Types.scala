package com.paranid5.vocabulary.routing.utils

import cats.effect.IO

import com.paranid5.vocabulary.di.AppDependencies
import com.paranid5.vocabulary.domain.User

import org.http4s.{AuthedRoutes, HttpRoutes, Request, Response}

private[routing] type AppRoutes       = AppDependencies[HttpRoutes[IO]]
private[routing] type AppAuthRoutes = AppDependencies[AuthedRoutes[User, IO]]
private[routing] type AppHttpResponse = AppDependencies[IO[Response[IO]]]
