package transformer

import play.api.libs.json.{JsValue, Json}
import services.MongoStorageService
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

class AssociateUrlsToUserId {

  def getUrlsAssociatedData(urlData: String, mongoStorageService: MongoStorageService) = {
    println(">>>>>>>association process started")
    val urlDataInJson = Json.parse(urlData)
    val urlsDataSeq = (urlDataInJson \ "url_data").as[Seq[JsValue]]
    println(">>>>>>parsing into json completed")
    var presnetCount = 0
    var absentCount = 0
    val associatedUrlsToId = Future(urlsDataSeq.flatMap {
      urlData =>
        val link = (urlData \ "link").as[String]
        val entities = (urlData \ "entities").asOpt[Seq[String]]
        val tags = (urlData \ "tags").asOpt[Seq[String]]
        val userIdList = mongoStorageService.getUserIds(link)

//        mongoStorageService.insertGroups(userIdList)
        presnetCount = presnetCount + 1

        if (userIdList.nonEmpty) {
          presnetCount = presnetCount + 1
          println("-----presentCount: " + presnetCount)
          //          println("-----list: "+ link +userIdList)

          userIdList.map { userId =>
            val gender = mongoStorageService.getGender(userId)
            gender + " " + userId + " | " + link + " | " + makeString(entities) + " | " + makeString(tags)
          }
        }
        else {
          absentCount = absentCount + 1
          println("-----absentCount: " + absentCount)
          Seq("None" + " " +"None" + " | " + link + " | " + makeString(entities) + " | " + makeString(tags))
        }
    })
    print(">>>>> completed association")
        associatedUrlsToId
  }

  def makeString(field: Option[Seq[String]]) = {
    if (field.isDefined) field.get.mkString(",") else "None"
  }
}
