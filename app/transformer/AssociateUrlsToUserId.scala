package transformer

import play.api.libs.json.{JsValue, Json}
import services.MongoStorageService

class AssociateUrlsToUserId {

  def getUrlsAssociatedData(urlData: String, mongoStorageService: MongoStorageService) = {
    println(">>>>>>>association process started")
    val urlDataInJson = Json.parse(urlData)
    val urlsDataSeq = (urlDataInJson \ "url_data").as[Seq[JsValue]]
    println(">>>>>>parsing into json completed")
    var count = 0
    val associatedUrlsToId: Seq[String] = urlsDataSeq.map {
      urlData =>
        val link = (urlData \ "link").as[String]
        val urlId = (urlData \ "id").as[String]
        val userIdList = mongoStorageService.getUserIds(link)

        if (userIdList.nonEmpty) {
          count = count + 1
          println("-----count: \n" + count)
          //          println("-----list: "+ link +userIdList)
          userIdList.mkString(":") + " " + urlId + " " + link
        }
        else {
          println("-----in else")
          "NA" + " " + urlId + " " + link
        }
    }
    print(">>>>> completed association")
    associatedUrlsToId
  }

}
