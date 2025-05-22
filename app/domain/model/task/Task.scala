package domain.model.task

import domain.model.task.Status.Todo
import ixias.model._
import ixias.util.EnumStatus
import play.api.libs.json._
import ixias.util.json.JsonEnvReads

import java.time.LocalDateTime

case class Task(
  id:         Option[Task.Id],
  categoryId: Option[Category.Id],
  title:      String,
  body:       String        = "",
  state:      Status        = Todo,
  updatedAt:  LocalDateTime = NOW,
  createdAt:  LocalDateTime = NOW
)           extends EntityModel[Task.Id]
object Task extends JsonEnvReads {
  val Id = the[Identity[Id]]
  type Id = Long @@ Task

  implicit val writes: Writes[Task.Id] = (o: Task.Id) => JsNumber(Id.unwrap(o))
  implicit val reads: Reads[Id] = idAsNumberReads[Id]
}

sealed abstract class Status(val code: Short, val name: String) extends EnumStatus
object Status extends EnumStatus.Of[Status] {
  case object Todo    extends Status(code = 0, name = "TODO(着手前)")
  case object Ongoing extends Status(code = 1, name = "進行中")
  case object Done    extends Status(code = 2, name = "完了")

  implicit val writes: Writes[Status] = (o: Status) =>
    JsObject(Seq(
      "code" -> JsNumber(o.code),
      "name" -> JsString(o.name)
    ))

  object Reads extends JsonEnvReads
  implicit val reads: Reads[Status] = Reads.enumReads[Status](Status)
}
