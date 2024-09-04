package com.paranid5.vocabulary.di

import cats.data.Reader
import cats.effect.IO
import cats.syntax.all.*

import com.paranid5.vocabulary.data.IOTransactor

import io.github.cdimascio.dotenv.Dotenv

type AppDependencies[F] = Reader[AppModule, F]

final class AppModule(val dotenv: Dotenv):
  lazy val transactor:  IOTransactor = IOTransactor(dotenv)
  lazy val wordsModule: WordsModule  = WordsModule(transactor)

object AppModule:
  def apply[T](launch: AppModule ⇒ IO[T]): IO[Either[Throwable, T]] =
    launchDotenv((new AppModule(_)) >>> (launch(_)))

  private def launchDotenv[T](f: Dotenv ⇒ IO[T]): IO[Either[Throwable, T]] =
    for
      dotenvRes ← IO(Dotenv.load()).attempt
      res       ← dotenvRes.map(f).sequence
    yield res

