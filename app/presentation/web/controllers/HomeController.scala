/**
  * to do sample project
  */

package presentation.web.controllers

import play.api.mvc._
import presentation.web.views.html.Task
import presentation.web.views.model.ViewValueHome
import usecase.task.{ShowCategoryUseCase, ShowTaskUseCase}

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

    val allTasksFuture      = showTaskUseCase.execute()
    val allCategoriesFuture = showCategoryUseCase.execute()
    val allStatus           = domain.model.task.Status.values
    for {
      allTasks      <- allTasksFuture
      allCategories <- allCategoriesFuture
    } yield Ok(Task(vv, allTasks, allCategories, allStatus))
  }
}
