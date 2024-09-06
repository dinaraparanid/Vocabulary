package com.paranid5.vocabulary.routing.utils.rate_limiter

import io.github.resilience4j.ratelimiter.{RateLimiter, RequestNotPermitted}

private final class Function0Limiter[A](
  rateLimiter: RateLimiter,
  function0:   () ⇒ A,
) extends (() ⇒ Either[Throwable, A]):
  def apply: Either[Throwable, A] =
    Function0Limiter.apply[A](rateLimiter, function0)

  def pure: Either[RequestNotPermitted, A] =
    Function0Limiter.pure[A](rateLimiter, function0)

private object Function0Limiter:
  def pure[A](
    rateLimiter: RateLimiter,
    function0:   () ⇒ A,
  ): Either[RequestNotPermitted, A] =
    try
      RateLimiter.waitForPermission(rateLimiter)
      Right(function0())
    catch
      case e: RequestNotPermitted ⇒ Left(e)

  def apply[A](
    rateLimiter: RateLimiter,
    function0:   () ⇒ A,
  ): Either[Throwable, A] =
    try
      RateLimiter.waitForPermission(rateLimiter)
      Right(function0.apply())
    catch
      case e: Throwable ⇒ Left(e)

private final class Function1Limiter[A, T](
  rateLimiter: RateLimiter,
  function1:   T ⇒ A,
) extends (T ⇒ Either[Throwable, A]):
  def apply(t: T): Either[Throwable, A] =
    Function1Limiter.apply(rateLimiter, function1, t)

  def pure(t: T): Either[RequestNotPermitted, A] =
    Function1Limiter.pure(rateLimiter, function1, t)

private object Function1Limiter:
  def pure[A, T](
    rateLimiter: RateLimiter,
    function1:   T ⇒ A,
    t:           T,
  ): Either[RequestNotPermitted, A] =
    try
      RateLimiter.waitForPermission(rateLimiter)
      Right(function1(t))
    catch
      case e: RequestNotPermitted ⇒ Left(e)

  def apply[A, T](
    rateLimiter: RateLimiter,
    function1:   T ⇒ A,
    t:           T,
  ): Either[Throwable, A] =
    try
      RateLimiter.waitForPermission(rateLimiter)
      Right(function1.apply(t))
    catch
      case e: Throwable ⇒ Left(e)
