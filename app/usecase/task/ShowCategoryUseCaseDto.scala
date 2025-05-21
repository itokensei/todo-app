package usecase.task

import domain.model.task.{ Category, Color }

case class ShowCategoryUseCaseDto(id: Category.Id, name: String, slug: String, color: Color)
