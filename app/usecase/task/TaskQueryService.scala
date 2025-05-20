package usecase.task

import domain.model.task.{ Category, Task }

import scala.concurrent.Future

trait TaskQueryService {
  def fetchAll(): Future[Seq[ShowTaskUseCaseDto]]

  /**
    * Get Task Data
    */
  def getById(id: Task.Id): Future[Option[Task]]

  def fetchAllCategoris(): Future[Seq[ShowCategoryUseCaseDto]]

  def getCategoryById(categoryId: Category.Id): Future[Option[Category]]
}
