package presentation.controllers

import domain.model.task.{ Category, Status, Task }
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.Reads.pattern
import play.api.libs.json.{ JsPath, Reads }

case class UpdateTaskRequest(
  id:         Task.Id,
  title:      String,
  body:       String,
  state:      Status,
  categoryId: Option[Category.Id]
)
object UpdateTaskRequest {
  implicit val reads: Reads[UpdateTaskRequest] = (
    (JsPath \ "id").read[Task.Id] and
      (JsPath \ "title").read[String](pattern(
        "^[a-zA-Z0-9\u3041-\u309A\u30A1-\u30FF\u4E00-\u9FAF]+$".r,
        "The title must contain only alphanumeric characters (a-z, A-Z, 0-9), Hiragana, Katakana, or Kanji. Space and newline characters are prohibited."
      )) and
      (JsPath \ "body").read[String](pattern(
        "^[a-zA-Z0-9\u3041-\u309A\u30A1-\u30FF\u4E00-\u9FAF \u3000\r\n]*$".r,
        "The body must consist of alphanumeric characters (a-z, A-Z, 0-9), Hiragana, Katakana, Kanji, spaces, or newlines."
      )) and
      (JsPath \ "state").read[Status] and
      (JsPath \ "categoryId").readNullable[Category.Id]
  )(UpdateTaskRequest.apply _)
}
