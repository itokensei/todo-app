package usecase.task

import domain.model.task.{ Category, Task, TaskRepository }

import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class AddTaskUseCase @Inject() (
  taskRepository:                TaskRepository,
  taskQueryService:              TaskQueryService,
  implicit val executionContext: ExecutionContext
) {
  def execute(taskWithNoId: Task#WithNoId): Future[ShowTaskUseCaseDto] = {
    val task           = taskWithNoId.v
    val categoryFuture = task.categoryId.fold(Future(Option.empty[Category]))(taskQueryService.getCategoryById)
    for {
      _        <- taskRepository.add(taskWithNoId)
      category <- categoryFuture
    } yield ShowTaskUseCaseDto(
      title         = task.title,
      body          = task.body,
      state         = task.state,
      categoryName  = category.map(_.name),
      categoryColor = category.map(_.color)
    )
  }
}
