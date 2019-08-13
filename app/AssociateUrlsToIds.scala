
import dataaccess.MongoDB
import fileOperations.FileOperations
import services.MongoStorageService
import transformer.AssociateUrlsToUserId
import utils.Constants
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

object AssociateUrlsToIds {

  def main(args: Array[String]): Unit = {
    println("hello Fukrey")
    val mongoDB = new MongoDB

    val mongoStorageService = new MongoStorageService(mongoDB)
    val associateUrlsToUserId = new AssociateUrlsToUserId()
    val fileOperations = new FileOperations

    val urlData = fileOperations.readFromFile("/Users/alli01/my-projects/gender-recognition/resources/input/Urls_data2.json")
    val associatedUrlsToId = associateUrlsToUserId.getUrlsAssociatedData(urlData, mongoStorageService)


    associatedUrlsToId.onComplete {
      case Success(value) => {
        println(">>>>>writing started")
        fileOperations.writeToFile(value)
        println(">>>>>>>>>writing to the file is done successfully")
        mongoDB.client.close()
      }
      case Failure(exception) => {
        println("exception occured: "+ exception)
        mongoDB.client.close()
      }
    }

    println(">>>>>writing started")
    fileOperations.writeToFile(Await.result(associatedUrlsToId, Duration.Inf))
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
