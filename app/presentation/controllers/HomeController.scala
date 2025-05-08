/**
  * to do sample project
  */

package presentation.controllers

import domain.model.task.Task
import ixias.play.api.mvc.JsonHelper
import play.api.mvc._
import presentation.views
import presentation.views.model.ViewValueHome
import usecase.task.{ AddTaskUseCase, ShowCategoryUseCase, ShowTaskUseCase }

import javax.inject._
import scala.concurrent.Future

import cats.syntax.either._

@Singleton
class HomeController @Inject() (
  implicit
  val mcc:             MessagesControllerComponents,
  showTaskUseCase:     ShowTaskUseCase,
  showCategoryUseCase: ShowCategoryUseCase,
  addTaskUseCase:      AddTaskUseCase
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
    for {
      tasks      <- tasksFuture
      categories <- categoriesFuture
    } yield Ok(views.html.Task(vv, tasks, categories))
  }

  def add() = Action.async { implicit request =>
    val addTaskExecution = for {
      addTaskRequest <- JsonHelper.bindFromRequest[AddTaskRequest]
      task           <- Task.create(addTaskRequest).toEither.leftMap(errorMessages => BadRequest(errorMessages.toList.mkString("\n")))
    } yield addTaskUseCase.execute(task)
    addTaskExecution.fold(Future.successful, _.map(_ => Redirect(routes.HomeController.index())))
  }
}
