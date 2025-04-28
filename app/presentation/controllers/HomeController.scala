/**
  * to do sample project
  */

package presentation.controllers

import play.api.mvc._
import presentation.views.model.ViewValueHome
import presentation.views
import usecase.task.ShowTaskUseCase

import javax.inject._

@Singleton
class HomeController @Inject() (
  val controllerComponents: ControllerComponents,
  showTaskUseCase:          ShowTaskUseCase,
) extends BaseController {
  def index() = Action.async {
    val vv = ViewValueHome(
      title  = "Home",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )

    val future = showTaskUseCase.execute()
    future.map(allTasks => Ok(views.html.Task(vv, allTasks)))(controllerComponents.executionContext)
  }
}
