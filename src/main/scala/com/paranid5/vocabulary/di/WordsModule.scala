package com.paranid5.vocabulary.di

import com.paranid5.vocabulary.data.IOTransactor
import com.paranid5.vocabulary.data.words.MySqlWordsRepository

final class WordsModule(transactor: IOTransactor):
  lazy val wordsRepository: MySqlWordsRepository =
    MySqlWordsRepository()
