package controllers

import javax.inject._
import play.api.mvc._
import scalikejdbc._
import scalikejdbc.config._
import models.ApplicationDao

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  // Load `application.conf` and setup.
  DBs.setupAll()

  def add(name: String) = Action { implicit request: Request[AnyContent] =>
    ApplicationDao.insertName(name)
    Ok(views.html.index("maybe success insert"))
  }

  def applications = Action { implicit request: Request[AnyContent] =>
    val id = ApplicationDao.findBy
    Ok(views.html.index(s"id: {$id.toString}"))
  }
}
