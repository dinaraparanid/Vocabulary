ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.5.0"

lazy val root = (project in file("."))
  .settings(
    name := "Vocabulary",
    libraryDependencies ++= Seq(
      "org.typelevel"         %% "cats-effect"                   % "3.5.4",
      "org.http4s"            %% "http4s-dsl"                    % "0.23.27",
      "org.http4s"            %% "http4s-ember-server"           % "0.23.27",
      "org.http4s"            %% "http4s-circe"                  % "0.23.27",
      "io.circe"              %% "circe-generic"                 % "0.14.7",
      "io.circe"              %% "circe-literal"                 % "0.14.10",
      "org.tpolecat"          %% "doobie-core"                   % "1.0.0-RC5",
      "io.github.cdimascio"    % "dotenv-kotlin"                 % "6.4.1",
      "com.github.daddykotex" %% "courier"                       % "3.2.0",
      "mysql"                  % "mysql-connector-java"          % "8.0.33",
      "io.github.resilience4j" % "resilience4j-ratelimiter"      % "2.1.0",
      "org.scalatest"         %% "scalatest"                     % "3.2.18" % Test,
      "org.typelevel"         %% "cats-effect-testing-scalatest" % "1.5.0"  % Test,
    )
  )
