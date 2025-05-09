package usecase.task

import domain.model.task.{ Task, TaskRepository }

import javax.inject.{ Inject, Singleton }
import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class AddTaskUseCase @Inject() (taskRepository: TaskRepository, implicit val executionContext: ExecutionContext) {
  def execute(task: Task#WithNoId): Future[Unit] = taskRepository.add(task).map(_ => ())
}
