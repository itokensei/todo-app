package usecase.task

import cats.data.EitherT
import domain.model.task.TaskRepository
import play.api.libs.json.JsError
import play.api.libs.json.JsResult.Exception
import presentation.controllers.DeleteTaskRequest
import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class DeleteTaskUseCase @Inject() (
  taskRepository:                TaskRepository,
  implicit val executionContext: ExecutionContext
) {
  def execute(deleteTaskRequest: DeleteTaskRequest): Future[Unit] = EitherT.fromOptionF(
    taskRepository.remove(deleteTaskRequest.id),
    Exception(JsError("No tasks were deleted in the DB"))
  ).map(_ => ()).rethrowT
}
