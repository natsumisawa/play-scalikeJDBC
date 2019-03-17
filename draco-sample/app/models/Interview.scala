package models

import java.time.LocalDateTime
import scalikejdbc._
import scalikejdbc.config._

case class Interview(id: Long, applicationId: Long, createdAt: LocalDateTime)

object Interview extends SQLSyntaxSupport[Interview] {

  override val schemaName: Option[String] = None
  override val tableName: String = "interviews"
  override val columns = Seq("id", "application_id", "created_at")

  def apply(int: ResultName[Interview])(rs: WrappedResultSet): Interview = {
    autoConstruct(rs, int)
  }
}

object InterviewDao {

  val (int, app) = (Interview.syntax("int"), Application.syntax("app"))

  def list: List[Interview] = {
    DB localTx { implicit session =>
      withSQL {
        select.from(Interview as int)
      }.map(Interview(int.resultName)).list.apply()
    }
  }

  def add(applicationId: Long): Unit = {
    val int = Interview.column
    DB localTx { implicit session =>
      withSQL {
        insert.into(Interview).columns(int.applicationId, int.createdAt)
          .values(applicationId, LocalDateTime.now())
      }.update.apply()
    }
  }

  def count: Int = {
    DB localTx { implicit session =>
      withSQL {
        select.from(Interview as int)
      }.map(_.long(1)).list.apply().length
    }
  }

  def findBy(applicationId: Long): List[Interview] = {
    DB localTx { implicit session =>
      withSQL {
        select.from(Interview as int)
          .innerJoin(Application as app).on(int.applicationId, app.id)
          .where.eq(int.applicationId, applicationId)
      }.map(Interview(int.resultName)).list.apply()
    }
  }
}
