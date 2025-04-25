package domain.model.task

import ixias.model._
import ixias.util.EnumStatus

import java.time.LocalDateTime

case class Task(id: Option[Task.Id], categoryId: Option[Category.Id], title: String, body: String = "", state: Status, updatedAt: LocalDateTime = NOW, createdAt: LocalDateTime = NOW)
  extends EntityModel[Task.Id]
object Task {
  val Id = the[Identity[Id]]
  type Id = Long @@ Task
}

sealed abstract class Status(val code: Short, val name: String) extends EnumStatus
object Status extends EnumStatus.Of[Status] {
  case object Todo    extends Status(code = 0, name = "TODO(着手前)")
  case object Ongoing extends Status(code = 1, name = "進行中")
  case object Done    extends Status(code = 2, name = "完了")
}
