package com.paranid5.vocabulary.di

import com.paranid5.vocabulary.data.words.MySqlWordsRepository

final class WordsModule:
  lazy val wordsRepository: MySqlWordsRepository =
    MySqlWordsRepository()
