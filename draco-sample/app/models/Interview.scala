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

  val int = Interview.syntax("int")

  def getInterviews: List[Interview] = {
    DB localTx { implicit session =>
      withSQL {
        select.from(Interview as int)
      }.map(Interview(int.resultName)).list.apply()
    }
  }
}
