package controllers

import javax.inject._
import java.time.LocalDateTime
import play.api._
import play.api.mvc._
import scalikejdbc._
import scalikejdbc.config._
import models.{ Application, Interview }

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  // Load `application.conf` and setup.
  DBs.setupAll()

  def createTable() = Action { implicit request: Request[AnyContent] =>
    // DB: auto get new connection
    // autoCommit: every operation will be executed
    DB autoCommit { implicit session =>
      sql"""
          create table applications (
            id serial not null primary key,
            name nvarchar(64) not null,
            created_at timestamp not null
          )
        """.execute.apply()
    } // already connection close
    Ok(views.html.index("maybe success create table"))
  }

  def insertName(name: String) = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index("maybe success insert"))
  }

  def selectName() = Action { implicit request: Request[AnyContent] =>
    val name = DB autoCommit { implicit session =>
      sql"select name from applications".map { app =>
        app.string("name")
      }.list.apply()
    }
    Ok(views.html.index(s"name:${name.toString}"))
  }
}
