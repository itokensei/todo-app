package presentation.controllers

import domain.model.task.Task
import play.api.libs.json.{ Json, Reads }

case class DeleteTaskRequest(id: Task.Id)
object DeleteTaskRequest {
  implicit def reads: Reads[DeleteTaskRequest] = Json.reads[DeleteTaskRequest]
}
