package models

import java.time.LocalDateTime
import scalikejdbc._
import scalikejdbc.config._

case class Application(id: Long, name: String, createdAt: LocalDateTime)

// scalikejdbc.SQLSyntaxSupport は ResultSet とモデル間の変換まわりの記述量を減らすために定義されている trait
// autoConstruct を使うとラップするところをマクロで書ける
object Application extends SQLSyntaxSupport[Application] {
  override val schemaName: Option[String] = None
  override val tableName: String = "applications"
  override val columns = Seq("ID", "name", "created_at")

  def apply(app: ResultName[Application])(rs: WrappedResultSet): Application = {
    autoConstruct(rs, app)
  }
}

object ApplicationDao {

  val app = Application.syntax("app")

  def insertName(name: String): Unit = {
    DB localTx { implicit session =>
      // --- tx start ---
      val app = Application.column
      withSQL {
        insert.into(Application).columns(app.name, app.createdAt)
          .values(name, LocalDateTime.now())
      }.update.apply()
      // --- tx end ---
    } // if throw exception => rollback
  }

  def count: Option[Long] = {
    DB localTx { implicit session =>
      withSQL {
        select.from(Application as app)
      }.map(_.long(1)).single.apply()
    }
  }

  def getApplications: List[Application] = {
    DB localTx { implicit session =>
      withSQL {
        select.from(Application as app)
      }.map(Application(app.resultName)).list.apply()
    }
  }
}