package usecase.task

import cats.implicits.none
import domain.model.task.{ Category, Task, TaskRepository }
import presentation.AddTaskRequest

import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class AddTaskUseCase @Inject() (
  taskRepository:                TaskRepository,
  taskQueryService:              TaskQueryService,
  implicit val executionContext: ExecutionContext
) {
  def execute(addTaskRequest: AddTaskRequest): Future[TaskItemDto] = {
    def taskWithNoId   = Task(
      id         = None,
      title      = addTaskRequest.title,
      body       = addTaskRequest.body,
      categoryId = addTaskRequest.categoryId
    ).toWithNoId
    val taskIdFuture   = taskRepository.add(taskWithNoId)
    val task           = taskWithNoId.v
    val categoryFuture = task.categoryId.fold(Future.successful(none[Category]))(taskQueryService.getCategoryById)
    for {
      taskId   <- taskIdFuture
      category <- categoryFuture
    } yield TaskItemDto(
      id            = taskId,
      title         = task.title,
      body          = task.body,
      state         = task.state,
      categoryId    = task.categoryId,
      categoryName  = category.map(_.name),
      categoryColor = category.map(_.color)
    )
  }
}
