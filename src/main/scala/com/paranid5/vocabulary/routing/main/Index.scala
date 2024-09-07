package com.paranid5.vocabulary.routing.main

import cats.data.Reader

import com.paranid5.vocabulary.domain.User
import com.paranid5.vocabulary.routing.utils.{AppHttpResponse, forbiddenIO}
import com.paranid5.vocabulary.routing.utils.rate_limiter.IORateLimiter

import doobie.syntax.all.*

import org.http4s.headers.`Content-Type`
import org.http4s.*
import org.http4s.dsl.io.*

import java.nio.charset.StandardCharsets
import java.util.Base64

private val rateLimitedOnIndex = IORateLimiter.limit0(onIndexImpl)

def onIndex(): AppHttpResponse =
  Reader: appModule ⇒
    rateLimitedOnIndex().fold(
      fa = _ ⇒ forbiddenIO,
      fb = _ run appModule
    ).flatten

private def onIndexImpl(): AppHttpResponse =
  Reader: appModule ⇒
    val wordsRepository = appModule.wordsModule.wordsRepository

    val response =
      for words ← wordsRepository.words.transact(appModule.transactor)
        yield Ok(
          page(words = words.map(_.text), admin = appModule.admin),
          `Content-Type`(MediaType.text.html)
        )

    response.flatten

private def page(
  words: List[String],
  admin: User,
): String =
  val adminCredentials = Base64.getEncoder.encodeToString:
    s"${admin.name}:${admin.password}".getBytes(StandardCharsets.UTF_8)

  s"""
     |<!DOCTYPE html>
     |<html lang="en">
     |<head>
     |  <meta charset="UTF-8">
     |  <meta name="viewport" content="width=device-width, initial-scale=1.0">
     |  <title>Vocabulary Builder</title>
     |
     |  <style>
     |    body {
     |        font-family: Arial, sans-serif;
     |        text-align: center;
     |    }
     |
     |    h1 {
     |        margin-bottom: 20px;
     |    }
     |
     |    input {
     |        padding: 10px;
     |        border: 1px solid #ccc;
     |        border-radius: 3px;
     |    }
     |
     |    button {
     |        padding: 10px;
     |        background-color: #333;
     |        color: #fff;
     |        border: none;
     |        border-radius: 3px;
     |        cursor: pointer;
     |    }
     |
     |  </style>
     |
     |</head>
     |<body>
     |
     |<h1>Your Vocabulary</h1>
     |
     |<input type="text" id="newWord" placeholder="Enter a new word">
     |<button id="addButton">Add Word</button>
     |<p></p>
     |<div id="wordList"></div>
     |
     |<script>
     |  const newWordInput = document.getElementById("newWord");
     |  const addButton = document.getElementById("addButton");
     |  const wordList = document.getElementById("wordList");
     |
     |  function addWord(word) {
     |    const listItem = document.createElement("li");
     |    listItem.textContent = word;
     |    wordList.appendChild(listItem);
     |    newWordInput.value = "";
     |  }
     |
     |  const words = '${words.mkString(",")}'.split(",");
     |  words.filter((word) => word !== "").forEach(addWord);
     |
     |  addButton.addEventListener("click", () => {
     |    const newWord = newWordInput.value;
     |    if (newWord) {
     |      const url = "http://0.0.0.0:8080/main/put?word=" + newWord;
     |
     |      try {
     |        const headers = {
     |          'Authorization': 'Basic $adminCredentials'
     |        };
     |
     |        fetch(url, { method: 'POST', headers: headers }).then(
     |          (response) => {
     |            if (response.ok)
     |              addWord(newWord);
     |          },
     |          (error) => {
     |            console.error("Error:", error);
     |          }
     |        );
     |      } catch (error) {
     |        console.error("Error:", error);
     |      }
     |    }
     |  });
     |</script>
     |</body>
     |</html>
     |""".stripMargin
