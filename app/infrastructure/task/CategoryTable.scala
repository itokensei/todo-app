package infrastructure.task

import domain.model.task.{ Category, Color }
import ixias.slick.jdbc.MySQLProfile.api._

import java.time.LocalDateTime

case class CategoryTable(tag: Tag) extends Table[Category](tag, "to_do_category") {
  def id        = column[Category.Id]("id", UInt64, O.AutoInc)
  def name      = column[String]("name", Utf8Char255)
  def slug      = column[String]("slug", AsciiChar64)
  def color     = column[Color]("color", UInt8)
  def updatedAt = column[LocalDateTime]("updated_at", TsCurrent)
  def createdAt = column[LocalDateTime]("created_at", Ts)

  def * = (id.?, name, slug, color, updatedAt, createdAt).<>(
    { case (id, name, slug, color, updatedAt, createdAt) =>
      Category(id = id, name = name, slug = slug, color = color, updatedAt = updatedAt, createdAt = createdAt)
    },
    (Category.unapply _).andThen(_.map(_.copy(
      _6 = LocalDateTime.now()
    )))
  )
}
