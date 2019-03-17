package controllers

import javax.inject._
import play.api.mvc._
import scalikejdbc.config._
import models.{ApplicationDao, InterviewDao}

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  // Load `application.conf` and setup.
  DBs.setupAll()

  def addApplication(name: String) = Action { implicit request: Request[AnyContent] =>
    val kariName = s"${scala.util.Random.alphanumeric.take(4).mkString}さん"
    ApplicationDao.add(kariName)
    val apps = ApplicationDao.list
    Ok(views.html.applicationList(apps))
  }

  def applications = Action { implicit request: Request[AnyContent] =>
    val apps = ApplicationDao.list
    Ok(views.html.applicationList(apps))
  }

  def addInterview(applciationId: Long) = Action { implicit req =>
    InterviewDao.add(applciationId)
    Ok(views.html.index("sucess!!"))
  }

  def interviews(applicationId: Long) = Action { implicit req =>
    val result = InterviewDao.findBy(applicationId)
    val msg = result.map(r => s"name: ${r._1} intId: ${r._2}")
    Ok(views.html.index(msg.toString))
  }
}
