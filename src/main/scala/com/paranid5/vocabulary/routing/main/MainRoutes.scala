package com.paranid5.vocabulary.routing.main

import cats.data.Reader
import cats.effect.IO

import com.paranid5.vocabulary.routing.utils.AppRoutes

import org.http4s.dsl.io.*
import org.http4s.server.middleware.CORS
import org.http4s.{HttpRoutes, Request, Response}

private object WordParamMatcher extends QueryParamDecoderMatcher[String]("word")

def mainRoutes: AppRoutes =
  Reader: appModule ⇒
    CORS.policy.withAllowOriginAll:
      HttpRoutes.of[IO]:
        case GET → (Root / "index.html") ⇒ onIndex() run appModule

        case POST → (Root / "put") // TODO: дополнить логином / паролем
          :? WordParamMatcher(word) ⇒ onPutWord(word) run appModule
