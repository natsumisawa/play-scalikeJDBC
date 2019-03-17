package models

import org.scalatest._
import org.scalatest.fixture.FlatSpec
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc.config._

class ApplicationSpec extends FlatSpec with Matchers with AutoRollback {

  DBs.setupAll()

  behavior of "add"

  it should "insert application" in { implicit session =>
    ApplicationDao.add("name")
    ApplicationDao.count should equal (1)
  }
}
