package ru.niklogik.crawler

import cats.effect._
import org.http4s.HttpApp
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router

object  CrawlerApp extends IOApp {

  val httpApp: HttpApp[IO] = Router("/" -> RootController.routes).orNotFound

  override def run(args: List[String]): IO[ExitCode] = {
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
  }
}