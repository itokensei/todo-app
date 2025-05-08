package domain.model.task

import cats.data.ValidatedNel
import cats.implicits.{ catsSyntaxApplyOps, catsSyntaxEither }
import domain.model.task.Status.Todo
import eu.timepit.refined.api.Refined
import eu.timepit.refined.refineV
import eu.timepit.refined.string.MatchesRegex
import ixias.model._
import ixias.util.EnumStatus
import presentation.controllers.AddTaskRequest

import java.time.LocalDateTime

case class Task(
  id:         Option[Task.Id],
  categoryId: Option[Category.Id],
  title:      Task.Title,
  body:       Task.Body     =
    refineV[Task.BodyRule]("").leftMap(error => new java.lang.Exception("invalid default value on Task.body\n" + error)).toTry.get,
  state:      Status        = Todo,
  updatedAt:  LocalDateTime = NOW,
  createdAt:  LocalDateTime = NOW
) extends EntityModel[Task.Id]
object Task {
  val Id = the[Identity[Id]]
  type Id = Long @@ Task

  // 英数字、ひらがな、カタカナ（長音符など含む）、漢字からいずれか１文字以上の文字列
  type Title     = String Refined TitleRule
  type TitleRule = MatchesRegex["^[a-zA-Z0-9\u3041-\u309A\u30A1-\u30FF\u4E00-\u9FAF]+$"]

  // 英数字、ひらがな、カタカナ（長音符など含む）、漢字、半角・全角空白文字および改行からいずれか０文字以上の文字列
  type Body     = String Refined BodyRule
  type BodyRule = MatchesRegex["^[a-zA-Z0-9\u3041-\u309A\u30A1-\u30FF\u4E00-\u9FAF \u3000\r]*$"]

  def create(addTaskRequest: AddTaskRequest): ValidatedNel[String, Task#WithNoId] = {
    val titleNel = refineV[TitleRule](addTaskRequest.title).toValidatedNel
    val bodyNel  = refineV[BodyRule](addTaskRequest.body).toValidatedNel
    (titleNel map2 bodyNel) { (validTitle, validBody) =>
      Task(
        id         = None,
        categoryId = addTaskRequest.categoryId,
        title      = validTitle,
        body       = validBody
      ).toWithNoId
    }
  }
}

sealed abstract class Status(val code: Short, val name: String) extends EnumStatus
object Status extends EnumStatus.Of[Status] {
  case object Todo    extends Status(code = 0, name = "TODO(着手前)")
  case object Ongoing extends Status(code = 1, name = "進行中")
  case object Done    extends Status(code = 2, name = "完了")
}
