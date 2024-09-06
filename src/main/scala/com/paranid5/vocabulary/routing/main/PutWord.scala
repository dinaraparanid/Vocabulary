package com.paranid5.vocabulary.routing.main

import cats.data.Reader
import cats.effect.IO

import com.paranid5.vocabulary.domain.User
import com.paranid5.vocabulary.routing.utils.AppHttpResponse

import doobie.syntax.all.*

import org.http4s.*
import org.http4s.dsl.io.*

def onPutWord(word: String): AppHttpResponse =
  Reader: appModule ⇒
    val wordsRepository = appModule.wordsModule.wordsRepository

    val response =
      for
        wordOpt ← wordsRepository
          .getWord(text = word)
          .transact(appModule.transactor)

        response ← wordOpt match
          case Some(_) ⇒ IO pure BadRequest()

          case None ⇒
            wordsRepository
              .storeWord(text = word)
              .transact(appModule.transactor)
              .map(_ ⇒ Created())
      yield response

    response.flatten
