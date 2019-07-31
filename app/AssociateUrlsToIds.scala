
import dataaccess.MongoDB
import fileOperations.FileOperations
import services.MongoStorageService
import transformer.AssociateUrlsToUserId

object AssociateUrlsToIds {

  def main(args: Array[String]): Unit = {
    println("hello Fukrey")
    val mongoDB = new MongoDB

    val mongoStorageService = new MongoStorageService(mongoDB)
    val associateUrlsToUserId = new AssociateUrlsToUserId()
    val fileOperations = new FileOperations

    val urlData = fileOperations.readFromFile("/Users/alli01/my-projects/gender-recognition/resources/input/url_data_1.json")
    val associatedUrlsToId = associateUrlsToUserId.getUrlsAssociatedData(urlData, mongoStorageService)
    mongoDB.client.close()

    println(">>>>>writing started")
    fileOperations.writeToFile(associatedUrlsToId)
    println(">>>>>>>>>writing to the file is done successfully")

  }

}


//  private def getKeyValue(userIdToUrlData: String) = {
//    println(">>>>>>> map building started")
//    val userIdToUrlMap = scala.collection.mutable.Map[String, String]()
//    val userIdToUrlDataSeq: Seq[String] = userIdToUrlData.split("\n").toSeq.map(_.trim)
//    println(">>>>>>>seq lines number" + userIdToUrlDataSeq.length)
//      userIdToUrlDataSeq.flatMap{
//        userToUrl =>
//          val splittedData: Seq[String] =  userToUrl.split(",").toList
//          userIdToUrlMap += (splittedData.last.trim -> splittedData.head.trim)
//      }
//    println(">>>>>>>>map building completed")
//    userIdToUrlMap
//  }
