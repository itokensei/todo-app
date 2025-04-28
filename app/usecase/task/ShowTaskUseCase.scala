package usecase.task

import javax.inject.{ Inject, Singleton }
import scala.concurrent.Future

@Singleton
class ShowTaskUseCase @Inject() (taskQueryService: TaskQueryService) {
  def execute(): Future[Seq[ShowTaskUseCaseDto]] = taskQueryService.fetchAll()
}
