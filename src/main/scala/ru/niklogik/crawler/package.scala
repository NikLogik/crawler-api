package ru.niklogik

package object crawler {

  case class TitlesRequest(urls: Array[String])
  case class TitlesResponse(titles: List[Title], errors: List[TitleError])

  case class Title(url: String, title: String)
  case class TitleError(url: String, error: String)

}
