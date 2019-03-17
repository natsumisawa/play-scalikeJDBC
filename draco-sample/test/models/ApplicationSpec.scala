package models

import org.scalatest._
import org.scalatest.fixture.FlatSpec
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc.config._
/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class ApplicationSpec extends FlatSpec with Matchers with AutoRollback {

  DBs.setupAll()

  behavior of "insert"

  it should "insert name to applications" in { implicit session =>
    val before = ApplicationDao.count
    ApplicationDao.insertName("alice")
    ApplicationDao.count should equal (before + 1)
  }
}
