package infrastructure.task

import domain.model.task.{ Category, Task }
import ixias.slick.SlickRepository
import ixias.slick.jdbc.MySQLProfile.api._
import usecase.task.{ ShowCategoryUseCaseDto, ShowTaskUseCaseDto, TaskQueryService }

import javax.inject.{ Inject, Named, Singleton }
import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class TaskQueryServiceImpl @Inject() (
  @Named("slave") slave: Database
)(implicit val ec:       ExecutionContext) extends SlickRepository[Task.Id, Task] with TaskQueryService {
  val taskTable     = TableQuery[TaskTable]
  val categoryTable = TableQuery[CategoryTable]

  def fetchAll(): Future[Seq[ShowTaskUseCaseDto]] = {
    val showData = for {
      (task, category) <- taskTable joinLeft categoryTable on (_.categoryId === _.id)
    } yield (task.title, task.body, task.state, category.map(_.name), category.map(_.color))
    slave.run(showData.result).map(_.map((ShowTaskUseCaseDto.apply _).tupled))
  }

  /**
    * Get Task Data
    */
  def getById(id: Task.Id): Future[Option[Task]] = {
    slave.run(taskTable.filter(_.id === id).result.headOption)
  }

  def fetchAllCategoris(): Future[Seq[ShowCategoryUseCaseDto]] = {
    val showData = categoryTable.map(category => (category.id, category.name, category.slug, category.color))
    slave.run(showData.result).map(_.map((ShowCategoryUseCaseDto.apply _).tupled))
  }
}
