package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import java.time.LocalDateTime
import models.{ Application, Interview }

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index() = Action { implicit request: Request[AnyContent] =>

    import scalikejdbc._
    import scalikejdbc.config._

    // Load `application.conf` and setup.
    DBs.setupAll()

    // DB: auto get new connection
    // autoCommit: every operation will be executed
    DB autoCommit {implicit session =>
      sql"""
        create table applications (
          id serial not null primary key,
          name nvarchar(64) not null,
          created_at timestamp not null
        )
      """.execute.apply()
    } // already connection close

    DB localTx { implicit session =>
      // --- tx start ---
      val (app, int) = (Application.column, Interview.column)
      withSQL {
        insert.into(Application).columns(app.name, app.createdAt)
          .values("Alice", LocalDateTime.now())
      }.update.apply()
      // --- tx end ---
    } // if throw exception => rollback

    val name = DB autoCommit { implicit session =>
      sql"select name from applications".map { app =>
        app.string("name")
      }.list.apply()
    }

    Ok(views.html.index(name.toString))
  }
}
