package usecase.task

import domain.model.task.Status
import play.api.libs.json.{ Json, OWrites }

case class ShowTaskUseCaseDto(allTasks: Seq[TaskItemDto], allStatus: Seq[Status], allCategories: Seq[ShowCategoryUseCaseDto])
object ShowTaskUseCaseDto {
  implicit val writes: OWrites[ShowTaskUseCaseDto] = Json.writes[ShowTaskUseCaseDto]
}
