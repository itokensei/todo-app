package domain.model.task

import scala.concurrent.Future

trait TaskRepository {

  /**
    * Add Task Data
    */
  def add(task: Task#WithNoId): Future[Task.Id]

  /**
    * Update Task Data
    */
  def update(entity: Task#EmbeddedId): Future[Option[Task#EmbeddedId]]

  /**
    * Delete Task Data
    */
  def remove(id: Task.Id): Future[Option[Task#EmbeddedId]]
}
