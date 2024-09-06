package com.paranid5.vocabulary.routing.auth

import cats.data.{Kleisli, OptionT}
import cats.effect.IO

import com.paranid5.vocabulary.domain.User

import org.http4s.{AuthedRequest, AuthedRoutes, BasicCredentials, Request, Response, Status}
import org.http4s.headers.Authorization
import org.http4s.server.AuthMiddleware

def authMiddleware: AuthMiddleware[IO, User] =
  AuthMiddleware(authUser = authUser, onFailure = onFailure)

private def authUser: Kleisli[IO, Request[IO], Either[String, User]] =
  def withAuthHeader(header: Authorization): IO[Either[String, User]] =
    header match
      case Authorization(BasicCredentials((name, password))) ⇒
        IO pure Right(User(name = name, password = password))
      case _ ⇒ IO pure Left("No basic credentials")

  Kleisli:
    _.headers.get[Authorization] match
      case Some(value) ⇒ withAuthHeader(header = value)
      case None        ⇒ IO pure Left("Unauthorized")

private def onFailure: AuthedRoutes[String, IO] =
  Kleisli: (_: AuthedRequest[IO, String]) ⇒
    OptionT pure Response[IO](status = Status.Unauthorized)
