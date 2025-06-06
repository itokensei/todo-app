package infrastructure.task

import domain.model.task.{ Category, Status, Task }
import ixias.slick.jdbc.MySQLProfile.api._

import java.time.LocalDateTime

case class TaskTable(tag: Tag) extends Table[Task](tag, "to_do") {
  def id         = column[Task.Id]("id", UInt64, O.PrimaryKey, O.AutoInc)
  def categoryId = column[Option[Category.Id]]("category_id", UInt64)
  def title      = column[String]("title", Utf8Char255)
  def body       = column[String]("body", Text)
  def state      = column[Status]("state", UInt8)
  def updatedAt  = column[LocalDateTime]("updated_at", TsCurrent)
  def createdAt  = column[LocalDateTime]("created_at", Ts)

  def * = (id.?, categoryId, title, body, state, updatedAt, createdAt).<>(
    (Task.apply _).tupled,
    (Task.unapply _).andThen(_.map(_.copy(
      _6 = LocalDateTime.now()
    )))
  )
}
