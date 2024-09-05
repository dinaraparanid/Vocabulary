package com.paranid5.vocabulary.data.words

import com.paranid5.vocabulary.data.ops.*
import com.paranid5.vocabulary.domain.Word

import doobie.ConnectionIO
import doobie.implicits.toSqlInterpolator

final class MySqlWordsRepository

object MySqlWordsRepository:
  given WordsRepository[ConnectionIO, MySqlWordsRepository] with
    extension (repository: MySqlWordsRepository)
      override def createTable(): ConnectionIO[Unit] =
        sql"""
        CREATE TABLE IF NOT EXISTS Vocabulary.Word (
          text VARCHAR(50) PRIMARY KEY
        )
        """.effect
        
      override def words: ConnectionIO[List[Word]] =
        sql"""SELECT * FROM Vocabulary.Word""".list

      override def getWord(text: String): ConnectionIO[Option[Word]] =
        sql"""SELECT * FROM Vocabulary.Word WHERE text = $text""".option

      override def storeWord(text: String): ConnectionIO[Unit] =
        sql"""
        INSERT INTO Vocabulary.Word (text)
        VALUES ($text)
        """.effect
