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

    /**
      * DB: 新しいコネクションを自動的に取得する
      */
    DB autoCommit {implicit session =>
      sql"""
        create table applications (
          id serial not null primary key,
          name nvarchar(64) not null,
          created_at timestamp not null
        )
      """.execute.apply()
    }
    // この時点ではコネクションは既に close されている

    /**
      * localTx: トランザクションブロック
      */
    DB localTx { implicit session =>
      // --- トランザクション開始 ---
      val (app, int) = (Application.column, Interview.column)
      withSQL {
        insert.into(Application).columns(app.name, app.createdAt)
          .values("Alice", LocalDateTime.now())
      }.update.apply()
      // --- トランザクション終了 ---
    } // 途中で例外が throw されたらロールバック

    Ok(views.html.index("hello"))
  }
}
