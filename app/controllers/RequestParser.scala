package controllers

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._


case class UserRequest(id : Long,
                       firstName : String,
                       lastName : String,
                       phoneNumber : String)

object UserRequest {
  implicit val userRequestReads: Reads[UserRequest] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "firstName").read[String] and
      (JsPath \ "lastName").read[String] and
      (JsPath \ "phoneNumber").read[String]
    )(UserRequest.apply _)
}