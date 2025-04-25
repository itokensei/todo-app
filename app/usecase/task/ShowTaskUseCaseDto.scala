package usecase.task

import domain.model.task.{Color, Status}

case class ShowTaskUseCaseDto(title: String, body: String, state: Status, categoryName: String, categoryColor: Color)
