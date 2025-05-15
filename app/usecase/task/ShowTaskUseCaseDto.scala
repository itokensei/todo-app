package usecase.task

import domain.model.task.{ Color, Status }
import play.api.libs.json.{ Json, OWrites }

case class ShowTaskUseCaseDto(
  title:         String,
  body:          String,
  state:         Status,
  categoryName:  Option[String],
  categoryColor: Option[Color]
)
object ShowTaskUseCaseDto {
  implicit def writes: OWrites[ShowTaskUseCaseDto] = Json.writes[ShowTaskUseCaseDto]
}
