package com.paranid5.vocabulary.routing.utils

import cats.effect.IO
import org.http4s.Response
import org.http4s.dsl.io.*

private def invalidBody: IO[Response[IO]] =
  BadRequest("Invalid body")
