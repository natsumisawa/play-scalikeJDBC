package models

import scalikejdbc._
import java.time.LocalDateTime
import sqls.count

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

  def insertName(name: String): Unit = {
    DB localTx { implicit session =>
      // --- tx start ---
      val (app, int) = (Application.column, Interview.column)
      withSQL {
        insert.into(Application).columns(app.name, app.createdAt)
          .values(name, LocalDateTime.now())
      }.update.apply()
      // --- tx end ---
    } // if throw exception => rollback
  }

  def count: Int = {
    DB autoCommit { implicit session =>
      sql"select name from applications".map { app =>
        app.string("name")
      }.list.apply().length
    }
  }

  def findBy: List[String] = {
    DB localTx { implicit session =>
      sql"select name from applications".map { app =>
        app.string("name")
      }.list.apply()
    }
  }
}