package com.paranid5.vocabulary.routing.utils.rate_limiter

import io.github.resilience4j.ratelimiter.RateLimiter

object IORateLimiter:
  def limit0[A](
    function0:   () ⇒ A,
    rateLimiter: RateLimiter = appRateLimiter,
  ): IOFunction0Limiter[A] =
    IOFunction0Limiter[A](rateLimiter, function0)

  def limit1[A, B](
    function1:   B ⇒ A,
    rateLimiter: RateLimiter = appRateLimiter,
  ): IOFunction1Limiter[A, B] =
    IOFunction1Limiter[A, B](rateLimiter, function1)
