package usecase.task

import domain.model.task.Task

import scala.concurrent.Future

trait TaskQueryService {
  def fetchAll(): Future[Seq[ShowTaskUseCaseDto]]

  /**
   * Get Task Data
   */
  def getById(id: Task.Id): Future[Option[Task]]
}
