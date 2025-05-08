package infrastructure.task

import domain.model.task.{ Category, Status, Task }
import eu.timepit.refined.api.Refined
import ixias.slick.jdbc.MySQLProfile.api._
import slick.ast.BaseTypedType
import slick.jdbc.JdbcType

import java.time.LocalDateTime

case class TaskTable(tag: Tag) extends Table[Task](tag, "to_do") {
  def id         = column[Task.Id]("id", UInt64, O.PrimaryKey, O.AutoInc)
  def categoryId = column[Category.Id]("category_id", UInt64)
  def title      = column[Task.Title]("title", Utf8Char255)
  def body       = column[Task.Body]("body", Text)
  def state      = column[Status]("state", UInt8)
  def updatedAt  = column[LocalDateTime]("updated_at", TsCurrent)
  def createdAt  = column[LocalDateTime]("created_at", Ts)

  def * = (id.?, categoryId.?, title, body, state, updatedAt, createdAt).<>(
    (Task.apply _).tupled,
    (Task.unapply _).andThen(_.map(_.copy(
      _6 = LocalDateTime.now()
    )))
  )

  // String <-> String Refined _
  // ixias.slick.jdbc.MySQLProfile.SlickColumnTypeOps.ixiasIdAsStringColumnTypeを参考に定義
  implicit def refinedStringAsStringColumnType[T <: String Refined _](implicit
    ctag: reflect.ClassTag[T]
  ): JdbcType[T] with BaseTypedType[T] = {
    MappedColumnType.base[T, String](
      refinedString => refinedString.asInstanceOf[String],
      rawString => rawString.asInstanceOf[T]
    )
  }
}
