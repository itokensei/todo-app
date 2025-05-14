package usecase.task

import domain.model.task.{ Task, TaskRepository }
import presentation.controllers.AddTaskRequest

import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class AddTaskUseCase @Inject() (
  taskRepository:                TaskRepository,
  taskQueryService:              TaskQueryService,
  implicit val executionContext: ExecutionContext
) {
  def execute(addTaskRequest: AddTaskRequest): Future[TaskItemDto] = {
    def taskWithNoId = Task(
      id         = None,
      title      = addTaskRequest.title,
      body       = addTaskRequest.body,
      categoryId = addTaskRequest.categoryId
    ).toWithNoId
    val task         = taskWithNoId.v
    for {
      _        <- taskRepository.add(taskWithNoId)
      category <- taskQueryService.getCategoryById(task.categoryId)
    } yield TaskItemDto(
      title         = task.title,
      body          = task.body,
      state         = task.state,
      categoryName  = category.map(_.name),
      categoryColor = category.map(_.color)
    )
  }
}
