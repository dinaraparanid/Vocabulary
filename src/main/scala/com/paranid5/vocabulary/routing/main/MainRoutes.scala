package com.paranid5.vocabulary.routing.main

import cats.data.Reader
import cats.effect.IO

import com.paranid5.vocabulary.routing.utils.AppRoutes

import org.http4s.dsl.io.*
import org.http4s.server.middleware.CORS
import org.http4s.{HttpRoutes, Request, Response}

//def mainRoutes: AppRoutes =
//  Reader: appModule ⇒
//    CORS.policy.withAllowOriginAll:
//      HttpRoutes.of[IO]: // TODO: восстановление пароля
//        case GET → (Root / "root.html") ⇒ onRoot() run appModule
