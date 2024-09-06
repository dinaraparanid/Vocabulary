package com.paranid5.vocabulary.data

import com.paranid5.vocabulary.domain.User
import io.github.cdimascio.dotenv.Dotenv

object Admin:
  def fromDotenv(dotenv: Dotenv) =
    User(
      name     = dotenv `get` MySqlDbUser,
      password = dotenv `get` MySqlDbPassword,
    )
