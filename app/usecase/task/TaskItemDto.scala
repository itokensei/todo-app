package usecase.task

import domain.model.task.{ Color, Status }

case class TaskItemDto(
  title:         String,
  body:          String,
  state:         Status,
  categoryName:  Option[String],
  categoryColor: Option[Color]
)
