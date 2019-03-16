package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import models.{ Application, Interview }

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  
  def index() = Action { implicit request: Request[AnyContent] =>

    import scalikejdbc._
    import scalikejdbc.config._

    // Load `application.conf` and setup.
    DBs.setupAll()

    DB autoCommit {implicit session =>
      sql"""
        create table applications (
          id serial not null primary key,
          name nvarchar(64) not null,
          created_at timestamp not null
        )
      """.execute.apply()
    }

    Ok(views.html.index("hello"))
  }
}
