/**
  * to do sample project
  */

package presentation.controllers

import domain.model.task.Task
import ixias.play.api.mvc.JsonHelper
import play.api.mvc._
import presentation.views
import presentation.views.model.ViewValueHome
import usecase.task.{ AddTaskUseCase, ShowTaskUseCase }

import javax.inject._
import scala.concurrent.Future

@Singleton
class HomeController @Inject() (
  implicit
  val mcc:             MessagesControllerComponents,
  showTaskUseCase:     ShowTaskUseCase,
  addTaskUseCase:      AddTaskUseCase
) extends MessagesAbstractController(mcc) {
  implicit val ec: scala.concurrent.ExecutionContext = mcc.executionContext

  def index() = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val vv = ViewValueHome(
      title  = "Home",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )

    val future = showTaskUseCase.execute()
    future.map(allTasks => Ok(views.html.Task(vv, allTasks)))(controllerComponents.executionContext)
  }

  def add() = Action.async { implicit request =>
    JsonHelper.bindFromRequest[AddTaskRequest].fold(
      formWithErrors => Future(formWithErrors),
      addTaskRequest =>
        addTaskUseCase.execute(Task(addTaskRequest)).map(_ => Redirect(routes.HomeController.index()))
    )
  }
}
