package models

import java.time.LocalDateTime
import scalikejdbc._
import scalikejdbc.config._
import scalikejdbc.scalatest.AutoRollback
import org.scalatest._
import org.scalatest.fixture.FlatSpec

class InterviewSpec extends FlatSpec with Matchers with AutoRollback {

  DBs.setupAll()

  behavior of "insert"

  it should "insert to interviews" in { implicit session =>
    ApplicationDao.add("test1")
    InterviewDao.add(1)
    InterviewDao.count should equal (1)
  }

  behavior of "find"

  it should "if an application has 1 interview, find 1 interview by application_id" in { implicit session =>
    ApplicationDao.add("test2")
    InterviewDao.add(2)
    println(InterviewDao.findBy(2))
    InterviewDao.findBy(2).length should equal (1)
  }

  it should "if an application has 2 interviews, find 2 interviews by application_id" in { implicit session =>
    ApplicationDao.add("test3")
    InterviewDao.add(3)
    InterviewDao.add(3)
    InterviewDao.findBy(3).length should equal (2)
  }
}
