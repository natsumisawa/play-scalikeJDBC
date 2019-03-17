package models

import org.scalatest._
import org.scalatest.fixture.FlatSpec
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc.config._

class ApplicationSpec extends FlatSpec with Matchers with AutoRollback {

  DBs.setupAll()

  behavior of "insert"

  it should "insert name to applications" in { implicit session =>
    ApplicationDao.add("name")
    ApplicationDao.count should equal (1)
  }
}
