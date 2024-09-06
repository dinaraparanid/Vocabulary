package com.paranid5.vocabulary.routing.main

import cats.data.Reader
import cats.effect.IO
import cats.syntax.all.*

import com.paranid5.vocabulary.domain.User
import com.paranid5.vocabulary.routing.auth.authMiddleware
import com.paranid5.vocabulary.routing.utils.{AppAuthRoutes, AppRoutes}

import org.http4s.dsl.io.*
import org.http4s.server.middleware.CORS
import org.http4s.{AuthedRoutes, HttpRoutes, Request, Response}

private object WordParamMatcher extends QueryParamDecoderMatcher[String]("word")

def mainRoutes: AppRoutes =
  for
    public ← mainPublicRoutes
    auth   ← mainAuthRoutes
  yield public <+> auth

private def mainPublicRoutes: AppRoutes =
  Reader: appModule ⇒
    CORS.policy.withAllowOriginAll:
      HttpRoutes.of[IO]:
        case GET -> Root / "index.html" ⇒ onIndex() run appModule

private def mainAuthRoutes: AppRoutes =
  Reader: appModule ⇒
    def authRoutes: AuthedRoutes[User, IO] =
      AuthedRoutes.of[User, IO]:
        case POST -> Root / "put" :? WordParamMatcher(word) as _ ⇒
          onPutWord(word = word) run appModule

    authMiddleware(authRoutes)
