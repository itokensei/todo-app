package presentation.controllers

import domain.model.task.{Category, Status, Task}
import play.api.libs.json.{Json, Reads}

case class UpdateTaskRequest(
  id:         Task.Id,
  title:      String,
  body:       String,
  state:      Status,
  categoryId: Option[Category.Id]
)
object UpdateTaskRequest {
  implicit val reads: Reads[UpdateTaskRequest] = Json.reads[UpdateTaskRequest]
}
