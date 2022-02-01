package ru.niklogik

package object crawler {
  case class TitlesRequest(urls: Array[String])

  sealed trait TitlesResponse
  case class TitlesSuccess(titles: List[UrlItem]) extends TitlesResponse
  case class TitlesError(errors: List[UrlItem]) extends TitlesResponse
  case class UrlItem(url: String, value: String)
}
