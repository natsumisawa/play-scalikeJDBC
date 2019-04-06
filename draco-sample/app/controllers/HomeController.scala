package controllers

import javax.inject._
import play.api.mvc._
import scalikejdbc.config._
import models.{ApplicationDao, InterviewDao}
import java.math.BigInteger
import java.security.{KeyFactory, PublicKey}
import java.security.spec._
import java.util.Base64

import _root_.play.api.libs.json.{JsPath, Json, Reads, Writes}
import akka.http.scaladsl.model.headers.Authorization
import javax.inject._
import pdi.jwt.{Jwt, JwtAlgorithm}
import play.api.Logger
import play.api.libs.ws.WSClient
import play.api.mvc._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class HomeController @Inject()(
  wsClient: WSClient,
  cc: ControllerComponents
) (implicit ec: ExecutionContext) extends AbstractController(cc) {

  DBs.setupAll()

  def addApplication(name: String) = Action { implicit request: Request[AnyContent] =>
    val kariName = s"${scala.util.Random.alphanumeric.take(4).mkString}さん"
    ApplicationDao.add(kariName)
    val apps = ApplicationDao.list
    Ok(views.html.applicationList(apps))
  }

  def applications = Action { implicit request: Request[AnyContent] =>
    val apps = ApplicationDao.list
    Ok(views.html.applicationList(apps))
  }

  def addInterview(applciationId: Long) = Action { implicit req =>
    InterviewDao.add(applciationId)
    Ok(views.html.index("sucess!!"))
  }

  def interviews(applicationId: Long) = Action { implicit req =>
    val result = InterviewDao.findBy(applicationId)
    result match {
      case Nil => Ok(views.html.index("nothing interviews"))
      case _ => Ok(views.html.interviewList(result))
    }
  }
}

case class Rsa(alg: String, e: String, kid: String, kty: String, n: String, use: String)

case class RsaKeys(keys: List[Rsa])

object Rsa {
  implicit val rsaReads: Reads[Rsa] = Json.reads[Rsa]
  implicit val rsaListReads: Reads[RsaKeys] = Json.reads[RsaKeys]
}

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._

case class Payload(userName: String, email: String)

object Payload {
  implicit val payloadReads: Reads[Payload] = (
    (JsPath \ "cognito:username").read[String] and
      (JsPath \ "email").read[String]
    ) (Payload.apply _)
}

case class JsonParseError(msg: String) extends RuntimeException(msg)

case class UnAuthorizedError(msg: String) extends RuntimeException(msg)

case class Name(value: String)
