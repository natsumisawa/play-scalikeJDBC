package models

import java.time.LocalDateTime
import scalikejdbc._
import scalikejdbc.config._
import scalikejdbc.scalatest.AutoRollback
import org.scalatest._
import org.scalatest.fixture.FlatSpec

class InterviewSpec extends FlatSpec with Matchers with AutoRollback {

  DBs.setupAll()

  behavior of ">add"

  it should "insert to interviews" in { implicit session =>
    ApplicationDao.add("test1")
    InterviewDao.add(1)
    InterviewDao.count should equal (1)
  }

  it should "if application_id not exist, not insert" in { implicit session =>
    val appId = 100
    InterviewDao.add(appId)
    InterviewDao.findBy(appId).length should equal (0)
  }

  behavior of ">find"

  it should "if an application has 1 interview, find 1 interview by application_id" in { implicit session =>
    val appId = 2
    ApplicationDao.add("test2")
    InterviewDao.add(appId)
    InterviewDao.findBy(appId).length should equal (1)
  }

  it should "if an application has 2 interviews, find 2 interviews by application_id" in { implicit session =>
    val appId = 3
    ApplicationDao.add("test3")
    InterviewDao.add(appId)
    InterviewDao.add(appId)
    InterviewDao.findBy(appId).length should equal (2)
  }
}
