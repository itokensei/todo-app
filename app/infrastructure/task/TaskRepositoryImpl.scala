/**
  * This is a sample of Todo Application.
  */

package infrastructure.task

import domain.model.task.{ Task, TaskRepository }
import ixias.slick.SlickRepository
import ixias.slick.jdbc.MySQLProfile.api._
import usecase.task.ShowTaskUseCaseDto

import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class TaskRepositoryImpl @Inject() (
  @Named("master") master: Database,
  @Named("slave") slave:   Database
)(implicit val ec:         ExecutionContext) extends SlickRepository[Task.Id, Task] with TaskRepository {

  val taskTable     = TableQuery[TaskTable]
  val categoryTable = TableQuery[CategoryTable]

  def fetchAll() = {
    val showData = for {
      (task, category) <- taskTable join categoryTable on (_.categoryId === _.id)
    } yield (task.title, task.body, task.state, category.name, category.color)
    slave.run(showData.result).map(_.map((ShowTaskUseCaseDto.apply _).tupled))
  }

  /**
    * Get Task Data
    */
  def getById(id: Task.Id): Future[Option[Task]] = {
    slave.run(taskTable.filter(_.id === id).result.headOption)
  }

  /**
    * Add Task Data
    */
  def add(task: Task#WithNoId): Future[Task.Id] = {
    master.run(taskTable returning taskTable.map(_.id) += task.v)
  }

  /**
    * Update Task Data
    */
  def update(entity: Task#EmbeddedId): Future[Option[Task#EmbeddedId]] = {
    master.run {
      taskTable.filter(_.id === entity.id).update(entity.v).map(_ > 0).map {
        case true  => Some(entity)
        case false => None
      }
    }
  }

  /**
    * Delete Task Data
    */
  def remove(id: Task.Id): Future[Option[Task#EmbeddedId]] = {
    master.run {
      taskTable.filter(_.id === id).delete.map {
        case 0 => None
        case _ => Some(id.asInstanceOf[Task#EmbeddedId])
      }
    }
  }
}
