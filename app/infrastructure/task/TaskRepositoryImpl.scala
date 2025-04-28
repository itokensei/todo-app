/**
  * This is a sample of Todo Application.
  */

package infrastructure.task

import domain.model.task.{ Task, TaskRepository }
import ixias.slick.SlickRepository
import ixias.slick.jdbc.MySQLProfile.api._

import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }

@Singleton
class TaskRepositoryImpl @Inject() (
  @Named("master") master: Database,
)(implicit val ec:         ExecutionContext) extends SlickRepository[Task.Id, Task] with TaskRepository {

  val taskTable = TableQuery[TaskTable]

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
