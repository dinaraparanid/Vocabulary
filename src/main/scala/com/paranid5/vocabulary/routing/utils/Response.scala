package com.paranid5.vocabulary.routing.utils

import cats.effect.IO
import org.http4s.{Response, Status}
import org.http4s.dsl.io.*

private[routing] def invalidBodyIO: IO[Response[IO]] =
  BadRequest("Invalid body")

private[routing] def forbiddenIO: IO[Response[IO]] =
  IO pure Response[IO](status = Status.Forbidden)
