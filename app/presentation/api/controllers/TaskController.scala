package presentation.api.controllers

import ixias.play.api.mvc.JsonHelper
import play.api.libs.json.Json
import play.api.mvc._
import presentation.{ AddTaskRequest, DeleteTaskRequest, UpdateTaskRequest }
import usecase.task._

import javax.inject._
import scala.concurrent.Future

@Singleton
class TaskController @Inject() (
  val controllerComponents: ControllerComponents,
  showTaskUseCase:          ShowTaskUseCase,
  showCategoryUseCase:      ShowCategoryUseCase,
  addTaskUseCase:           AddTaskUseCase,
  updateTaskUseCase:        UpdateTaskUseCase,
  deleteTaskUseCase:        DeleteTaskUseCase
) extends BaseController {
  implicit val ec: scala.concurrent.ExecutionContext = controllerComponents.executionContext

  def list() = Action.async { implicit request =>
    val allTasksFuture      = showTaskUseCase.execute()
    val allCategoriesFuture = showCategoryUseCase.execute()
    val allStatus           = domain.model.task.Status.values
    for {
      allTasks      <- allTasksFuture
      allCategories <- allCategoriesFuture
    } yield Ok(Json.toJson(ShowTaskUseCaseDto(allTasks, allStatus, allCategories)))
  }

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

  def delete() = Action.async { implicit request =>
    JsonHelper.bindFromRequest[DeleteTaskRequest].fold(
      Future.successful,
      deleteTaskRequest =>
        deleteTaskUseCase.execute(deleteTaskRequest).map { case () => NoContent }
    )
  }
}
