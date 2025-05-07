package usecase.task

import domain.model.task.Color

case class ShowCategoryUseCaseDto(id: Long, name: String, slug: String, color: Color)
