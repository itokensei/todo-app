/**
  * to do sample project
  */

package presentation.controllers

import domain.model.task.{ Category, Task }
import ixias.model.tag
import ixias.play.api.mvc.JsonHelper
import play.api.data.Form
import play.api.data.Forms.{ longNumber, mapping, nonEmptyText, optional, text }
import play.api.mvc._
import presentation.views
import presentation.views.model.ViewValueHome
import usecase.task.{ AddTaskUseCase, ShowCategoryUseCase, ShowTaskUseCase }

import javax.inject._
import scala.concurrent.Future

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
    // type mismatch;
    // found   : x$1.type (with underlying type Long)
    // required: shapeless.tag.Tagged[domain.model.task.Category] with Long
    val form = Form(
      mapping(
        "title"      -> nonEmptyText,
        "body"       -> text,
        "categoryId" -> optional(longNumber)
      ) {
        case (title, body, rawCategoryId) => AddTaskRequest(title, body, rawCategoryId.map(tag[Category].apply(_)))
      }(request =>
        AddTaskRequest.unapply(request).map {
          case (title, body, Some(categoryId)) => (title, body, Some(categoryId.asInstanceOf[Long]))
          case x => x
        }
      )
    )

    JsonHelper.bindFromRequest[AddTaskRequest].fold(
      formWithErrors => Future(formWithErrors),
      addTaskRequest =>
        addTaskUseCase.execute(Task(addTaskRequest)).map(_ => Redirect(routes.HomeController.index()))
    )
  }
}
