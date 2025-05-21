package usecase.task

import domain.model.task.{Category, Color, Status, Task}
import play.api.libs.json.{Json, OWrites}

case class TaskItemDto(
  id:            Task.Id,
  title:         String,
  body:          String,
  state:         Status,
  categoryId : Option[Category.Id],
  categoryName:  Option[String],
  categoryColor: Option[Color]
)
object TaskItemDto {
  implicit def writes: OWrites[TaskItemDto] = Json.writes[TaskItemDto]
}
