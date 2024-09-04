package com.paranid5.vocabulary.data.words

import cats.Applicative
import com.paranid5.vocabulary.domain.Word

trait WordsRepository[F[_] : Applicative, R]:
  extension (repository: R)
    def createTable(): F[Unit]

    def getWord(text: String): F[Option[Word]]

    def storeWord(text: String): F[Unit]
