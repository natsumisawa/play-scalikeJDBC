package models

import java.time.LocalDateTime
import scalikejdbc._
import scalikejdbc.config._

case class Application(id: Long, applicationName: String, createdAt: LocalDateTime)

// scalikejdbc.SQLSyntaxSupport は ResultSet とモデル間の変換まわりの記述量を減らすために定義されている trait
// autoConstruct を使うとラップするところをマクロで書ける
object Application extends SQLSyntaxSupport[Application] {
  override val schemaName: Option[String] = None
  override val tableName: String = "applications"
  override val columns = Seq("id", "application_name", "created_at")

  def apply(app: ResultName[Application])(rs: WrappedResultSet): Application = {
    autoConstruct(rs, app)
  }
}

object ApplicationDao {

  val app = Application.syntax("app")

  def add(name: String): Unit = {
    DB localTx { implicit session =>
      // --- tx start ---
      val app = Application.column
      withSQL {
        insert.into(Application).columns(app.applicationName, app.createdAt)
          .values(name, LocalDateTime.now())
      }.update.apply()
      // --- tx end ---
    } // if throw exception => rollback
  }

  def count: Int = {
    DB localTx { implicit session =>
      withSQL {
        select.from(Application as app)
      }.map(_.long(1)).list.apply().length
    }
  }

  def list: List[Application] = {
    DB localTx { implicit session =>
      withSQL {
        select.from(Application as app)
      }.map(Application(app.resultName)).list.apply()
    }
  }
}