/**
  * to do sample project
  */

package presentation.controllers

import ixias.play.api.mvc.JsonHelper
import play.api.libs.json.Json
import play.api.mvc._
import usecase.task.{ AddTaskUseCase, UpdateTaskUseCase }

import javax.inject._
import scala.concurrent.Future

@Singleton
class TaskController @Inject() (
  val controllerComponents: ControllerComponents,
  addTaskUseCase:           AddTaskUseCase,
  updateTaskUseCase:        UpdateTaskUseCase
) extends BaseController {
  implicit val ec: scala.concurrent.ExecutionContext = controllerComponents.executionContext

  def add() = Action.async { implicit request =>
    JsonHelper.bindFromRequest[AddTaskRequest].fold(
      Future.successful,
      addTaskRequest =>
        addTaskUseCase.execute(addTaskRequest).map(task => Ok(Json.toJson(task)))
    )
  }

  def update() = Action.async { implicit request =>
    JsonHelper.bindFromRequest[UpdateTaskRequest].fold(
      Future.successful,
      updateTaskRequest =>
        updateTaskUseCase.execute(updateTaskRequest).map(task => Ok(Json.toJson(task)))
    )
  }
}
