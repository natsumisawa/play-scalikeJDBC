package controllers

import javax.inject._
import java.time.LocalDateTime
import play.api._
import play.api.mvc._
import scalikejdbc._
import scalikejdbc.config._
import models.{ Application, Interview, ApplicationDao }

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  // Load `application.conf` and setup.
  DBs.setupAll()

  def add(name: String) = Action { implicit request: Request[AnyContent] =>
    ApplicationDao.insertName(name)
    Ok(views.html.index("maybe success insert"))
  }

  def select(name: String) = Action { implicit request: Request[AnyContent] =>
//    val app = Application.column
//    DB localTx { implicit session =>
//      val result = withSQL {
//        select.from(Application as app)
//      }.map(Application(app.name)).list.apply()
//    }
    Ok(views.html.index("id"))
  }
}
