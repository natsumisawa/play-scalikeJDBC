package models

import scalikejdbc._
import java.time.ZonedDateTime

case class Application(id: Long, name: String, createdAt: ZonedDateTime)

// scalikejdbc.SQLSyntaxSupport は ResultSet とモデル間の変換まわりの記述量を減らすために定義されている trait
object Application extends SQLSyntaxSupport[Application] {
  override val schemaName: Option[String] = None
  override val tableName: String = "applications"

  def apply(user: ResultName[Application])(rs: WrappedResultSet): Application = {
    Application(rs.long(user.id), rs.string(user.name), rs.dateTime(user.createdAt))
  }
}

case class Interview(id: Long, applicationId: Long, createdAt: ZonedDateTime)

object Interview extends SQLSyntaxSupport[Interview] {

  override val schemaName: Option[String] = None
  override val tableName: String = "interviews"

  def apply(int: ResultName[Interview], app: ResultName[Application])(rs: WrappedResultSet): Interview = {
    Interview(rs.long(int.id), rs.long(int.applicationId), rs.dateTime(int.createdAt))
  }
}