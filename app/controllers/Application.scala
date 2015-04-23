package controllers

import java.util.Locale

import anorm._
import play.api.db.DB
import play.api.libs.json.{JsError, JsSuccess, JsValue}
import play.api.mvc._
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future
import play.api.libs.json._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def user = Action.async { implicit request =>
    var bodyJson: JsValue = null
    request.contentType.map(_.toLowerCase(Locale.ENGLISH)) match {
      case Some("application/json") => {
        bodyJson = request.body.asJson.get
        bodyJson.validate[UserRequest] match {
          case s: JsSuccess[UserRequest] => {
            val user = s.get
            DB.withConnection { implicit connection =>
              SQL( """INSERT INTO Employee
               (Employee_Id, First_Name, Last_Name, PhoneNumber)
                values ({employeeId}, {fname}, {lname}, {phone})""")
                .on(
                  "employeeId" -> user.id,
                  "fname" -> user.firstName,
                  "lname" -> user.lastName,
                  "phone" -> user.phoneNumber
                ).executeInsert()
              Future(Ok("Logged In"))
            }
          }
          case e: JsError => {
            throw new Exception
          }
        }
      }
      case _ => throw new Exception
    }
  }


  def users = Action.async { implicit request =>
    DB.withConnection { implicit connection =>
      val users  = SQL("SELECT * FROM Employee").as(models.User.parser.*)
      println("USERS >> "+users)
      Future(Ok(Json.toJson(users)))
    }
  }
}