package usecase.task

import domain.model.task.{ Category, Color }
import play.api.libs.json.{ Json, OWrites }

case class ShowCategoryUseCaseDto(id: Category.Id, name: String, slug: String, color: Color)
object ShowCategoryUseCaseDto {
  implicit val writes: OWrites[ShowCategoryUseCaseDto] = Json.writes[ShowCategoryUseCaseDto]
}
