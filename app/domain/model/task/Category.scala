package domain.model.task

import ixias.model._
import ixias.util.EnumStatus

import java.time.LocalDateTime

case class Category(id: Option[Category.Id], name: String, slug: String, color: Color, updatedAt: LocalDateTime = NOW, createdAt: LocalDateTime = NOW) extends EntityModel[Category.Id]
object Category {
  val Id = the[Identity[Id]]
  type Id = Long @@ Category
}

sealed abstract class Color(val code: Short, val hexCode: String) extends EnumStatus
object Color extends EnumStatus.Of[Color] {
  case object Red       extends Color(code = 0, hexCode = "#E74C3C")
  case object Orange    extends Color(code = 1, hexCode = "#E67E22")
  case object Yellow    extends Color(code = 2, hexCode = "#F1C40F")
  case object Green     extends Color(code = 3, hexCode = "#2ECC71")
  case object Blue      extends Color(code = 4, hexCode = "#3498DB")
  case object Purple    extends Color(code = 5, hexCode = "#9B59B6")
  case object LightBlue extends Color(code = 6, hexCode = "#5DADE2")
  case object Gray      extends Color(code = 7, hexCode = "#808080")
}
