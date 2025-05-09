package presentation.controllers

import domain.model.task.Category
import play.api.libs.json.{ Json, Reads }

case class AddTaskRequest(title: String, body: String, categoryId: Option[Category.Id])

object AddTaskRequest {
  implicit val reads: Reads[AddTaskRequest] = Json.reads[AddTaskRequest]
}
