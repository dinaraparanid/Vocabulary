package com.paranid5.vocabulary.routing.utils.rate_limiter

import io.github.resilience4j.ratelimiter.{RateLimiter, RateLimiterConfig}

import java.time.Duration

private val LimitPeriodSecs = 3
private val TimeoutDurationSecs = 10

val appRateLimiter = RateLimiter.of("3/second", appRateLimiterConfig)

private def appRateLimiterConfig: RateLimiterConfig =
  RateLimiterConfig
    .custom()
    .limitRefreshPeriod(Duration `ofSeconds` 1)
    .limitForPeriod(LimitPeriodSecs)
    .timeoutDuration(Duration `ofSeconds` TimeoutDurationSecs)
    .build()
