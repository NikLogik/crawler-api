ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

val http4sVersion = "0.23.7"
val circeVersion = "0.14.1"

lazy val root = (project in file("."))
  .settings(
    name := "crawler",
    libraryDependencies ++=Seq(
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "org.typelevel" %% "cats-effect" % "3.3.4",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-literal" % circeVersion,
      "org.jsoup" % "jsoup" % "1.14.3"
    )
  )

