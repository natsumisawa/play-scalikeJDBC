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
  override val columns = Seq("id", "name", "created_at")

  def apply(app: ResultName[Application])(rs: WrappedResultSet): Application = {
    autoConstruct(rs, app)
  }
}

object ApplicationDao extends SQLSyntaxSupport[Application] {

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

  def count: Int = DB autoCommit { implicit session =>
    sql"select name from applications".map { app =>
      app.string("name")
    }.list.apply().length
  }
}

case class Interview(id: Long, applicationId: Long, createdAt: LocalDateTime)

object Interview extends SQLSyntaxSupport[Interview] {

  override val schemaName: Option[String] = None
  override val tableName: String = "interviews"

  def apply(int: ResultName[Interview], app: ResultName[Application])(rs: WrappedResultSet): Interview = {
    autoConstruct(rs, int)
  }
}