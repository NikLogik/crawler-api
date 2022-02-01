package ru.niklogik.crawler

import cats.effect._
import fs2.io.IOException
import fs2.io.net.SocketTimeoutException
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe._
import org.http4s.dsl.io._
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import java.net.{MalformedURLException, UnknownHostException}
import scala.util.{Failure, Success, Try}

object RootController {

  implicit val decoder: EntityDecoder[IO, TitlesRequest] = jsonOf[IO, TitlesRequest]

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case request @ POST -> Root / "titles" => handle(request)
  }

  def handle(request: Request[IO]): IO[Response[IO]] = {
      request
        .as[TitlesRequest]
        .flatMap(findTitles)
        .flatMap(Ok(_))
  }

  def findTitles(requestBody: TitlesRequest): IO[TitlesResponse] = IO {
    var responseBody = List[Title]()
    var errors = List[TitleError]()

    for (url <- requestBody.urls) {
      val page = loadHtml(url)
      page match {
        case Failure(exception)  => errors = errors :+ TitleError(url, exception.getMessage)
        case Success(document) => responseBody = responseBody :+ Title(url, document.title())
      }
    }

    TitlesResponse(responseBody, errors)
  }

  def loadHtml(url: String): Try[Document] = {
    try {
      val document = Jsoup.connect(url).get()
      Success(document)
    } catch {
      case _: MalformedURLException => Failure(new IllegalArgumentException(s"Malformed URL: $url"))
      case _: UnknownHostException => Failure(new UnknownHostException(s"Unknown host: $url"))
      case _: SocketTimeoutException => Failure(new SocketTimeoutException(s"Connection timeout: $url"))
      case e: Throwable => Failure(e)
    }
  }
}
