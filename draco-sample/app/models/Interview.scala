package models

import java.time.LocalDateTime
import scalikejdbc._

case class Interview(id: Long, applicationId: Long, createdAt: LocalDateTime)

object Interview extends SQLSyntaxSupport[Interview] {

  override val schemaName: Option[String] = None
  override val tableName: String = "interviews"

  def apply(int: ResultName[Interview], app: ResultName[Application])(rs: WrappedResultSet): Interview = {
    autoConstruct(rs, int)
  }
}
