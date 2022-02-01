package ru.niklogik.crawler

import cats.effect._
import cats.effect.unsafe.IORuntime.global
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe._
import org.http4s.dsl.io._
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.util.{Failure, Success, Try}

case class UrlRequest(urls: Array[String])
case class UrlResponse(titles: Map[String, String])

sealed trait TitlesResponse
case class TitlesSuccess(titles: List[UrlItem]) extends TitlesResponse
case class TitlesError(errors: List[UrlItem]) extends TitlesResponse
case class UrlItem(url: String, value: String)

object WebService {

  implicit val decoder: EntityDecoder[IO, UrlRequest] = jsonOf[IO, UrlRequest]
  implicit val encoder: EntityEncoder[IO, UrlResponse] = jsonEncoderOf[IO, UrlResponse]

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case request @ POST -> Root / "titles" => handle(request)
  }

  def handle(request: Request[IO]): IO[Response[IO]] = {
      request
        .as[UrlRequest]
        .flatMap(findTitles)
        .flatMap(makeResponse)
  }

  def findTitles(requestBody: UrlRequest): IO[Either[TitlesError, TitlesSuccess]] = {
    var responseBody = List[UrlItem]()
    var errors = List[UrlItem]()

    for (url <- requestBody.urls) {
      val page = loadHtml(url)
      page match {
        case Failure(exception)  => errors = errors :+ UrlItem(url, exception.getMessage)
        case Success(document) => responseBody = responseBody :+ UrlItem(url, document.title())
      }
    }
    if (errors.nonEmpty) IO(Left(TitlesError(errors)))
    else IO(Right(TitlesSuccess(responseBody)))
  }

  def makeResponse(either: Either[TitlesError, TitlesSuccess]): IO[Response[IO]] = {
    either match {
      case Left(errors) => BadRequest(errors)
      case Right(response) => Ok(response)
    }
  }

  def loadHtml(url: String): Try[Document] = Try(Jsoup.connect(url).get())
}
