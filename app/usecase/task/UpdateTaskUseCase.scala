package usecase.task

import cats.implicits.none
import domain.model.task.{ Category, Task, TaskRepository }
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
    val newTask        = Task(
      id         = Some(updateTaskRequest.id),
      categoryId = updateTaskRequest.categoryId,
      title      = updateTaskRequest.title,
      body       = updateTaskRequest.body,
      state      = updateTaskRequest.state
    )
    val execution      = taskRepository.update(newTask.toEmbeddedId)
    val categoryFuture = newTask.categoryId.fold(Future.successful(none[Category]))(taskQueryService.getCategoryById)
    for {
      _        <- execution
      category <- categoryFuture
    } yield TaskItemDto(
      title         = newTask.title,
      body          = newTask.body,
      state         = newTask.state,
      categoryName  = category.map(_.name),
      categoryColor = category.map(_.color)
    )
  }
}
