package usecase.task

import cats.implicits.none
import domain.model.task.{ Category, Task, TaskRepository }
import play.api.libs.json.JsError
import play.api.libs.json.JsResult.Exception
import presentation.controllers.UpdateTaskRequest

import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class UpdateTaskUseCase @Inject() (
  taskRepository:                TaskRepository,
  taskQueryService:              TaskQueryService,
  implicit val executionContext: ExecutionContext
) {
  def execute(updateTaskRequest: UpdateTaskRequest): Future[TaskItemDto] = {
    val requestTask      = Task(
      id         = Some(updateTaskRequest.id),
      categoryId = updateTaskRequest.categoryId,
      title      = updateTaskRequest.title,
      body       = updateTaskRequest.body,
      state      = updateTaskRequest.state
    )
    val resultTaskFuture = taskRepository.update(requestTask.toEmbeddedId).flatMap {
      _.fold(Future.failed[Task#EmbeddedId](Exception(JsError("Failed to update the task on DB. "))))(Future.successful)
    }
    val categoryFuture   = requestTask.categoryId.fold(Future.successful(none[Category]))(taskQueryService.getCategoryById)
    for {
      resultTask <- resultTaskFuture
      category   <- categoryFuture
    } yield TaskItemDto(
      id            = resultTask.id,
      title         = resultTask.v.title,
      body          = resultTask.v.body,
      state         = resultTask.v.state,
      categoryId    = requestTask.categoryId,
      categoryName  = category.map(_.name),
      categoryColor = category.map(_.color)
    )
  }
}
