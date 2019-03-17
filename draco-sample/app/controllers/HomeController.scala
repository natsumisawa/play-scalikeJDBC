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

  def findBy(name: String) = Action { implicit request: Request[AnyContent] =>
    val id = ApplicationDao.findBy(name)
    Ok(views.html.index(s"id: {$id.toString}"))
  }

  private def createTable: Unit = {
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
  }
}
