/**
  * to do sample project
  */

package presentation.controllers

import domain.model.task.TaskRepository
import play.api.mvc._
import presentation.views.model.ViewValueHome
import presentation.views

import javax.inject._

@Singleton
class HomeController @Inject() (
  val controllerComponents: ControllerComponents,
  taskRepository:           TaskRepository,
) extends BaseController {
  def index() = Action.async {
    val vv = ViewValueHome(
      title  = "Home",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )

    val future = taskRepository.fetchAll()
    future.map(allTasks => Ok(views.html.Task(vv, allTasks)))(controllerComponents.executionContext)
  }
}
