package models

import anorm.{~, RowParser}
import anorm.SqlParser._
import play.api.libs.json.{Json, Writes}

case class User(employeeId:Long,
                         firstName: String,
                         lastName: String,
                         phoneNumber: String)

object User {
  val parser: RowParser[User] = {
    long("Employee_Id") ~
      str("First_Name") ~
      str("Last_Name") ~
      str("PhoneNumber") map {
      case employeeId ~ firstName ~ lastName ~ phoneNumber =>
        User(employeeId, firstName, lastName, phoneNumber)
    }
  }

  implicit val userWrites = new Writes[User] {
    def writes(user: User) = Json.obj(
      "employeeId" -> user.employeeId,
      "firstName" -> user.firstName,
      "lastName" -> user.lastName,
      "phoneNumber" -> user.phoneNumber
    )
  }
}
