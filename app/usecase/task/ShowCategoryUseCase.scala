package usecase.task

import javax.inject.{ Inject, Singleton }
import scala.concurrent.Future

@Singleton
class ShowCategoryUseCase @Inject()(taskQueryService: TaskQueryService) {
  def execute(): Future[Seq[ShowCategoryUseCaseDto]] = taskQueryService.fetchAllCategoris()
}
