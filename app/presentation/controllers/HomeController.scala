/**
  * to do sample project
  */

package presentation.controllers

import play.api.mvc._
import presentation.views
import presentation.views.model.ViewValueHome
import usecase.task.{ ShowCategoryUseCase, ShowTaskUseCase }

import javax.inject._

@Singleton
class HomeController @Inject() (
  implicit
  val mcc:             MessagesControllerComponents,
  showTaskUseCase:     ShowTaskUseCase,
  showCategoryUseCase: ShowCategoryUseCase
) extends MessagesAbstractController(mcc) {
  implicit val ec: scala.concurrent.ExecutionContext = mcc.executionContext

  def index() = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val vv = ViewValueHome(
      title  = "Home",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )

    val tasksFuture      = showTaskUseCase.execute()
    val categoriesFuture = showCategoryUseCase.execute()
    val allState         = domain.model.task.Status.values
    for {
      tasks      <- tasksFuture
      categories <- categoriesFuture
    } yield Ok(views.html.Task(vv, tasks, categories, allState))
  }
}
