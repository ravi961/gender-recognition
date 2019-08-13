package services

import java.util

import akka.actor.FSM.Failure
import dataaccess.{MongoDB}
import javax.inject.Inject
import org.bson.{BsonArray, BsonString, BsonValue}
import org.mongodb.scala.bson.{BsonArray, BsonDocument}
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.{Completed, Document, FindObservable, MongoCollection, Observer}
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

class MongoStorageService @Inject()(mongoConnection: MongoDB) {
//  val userIdCollection: MongoCollection[Document] = mongoConnection.getMongoCollection("url-to-userId")
  val userIdCollection: MongoCollection[Document] = mongoConnection.getMongoCollection("aggregate-results")
  val trainDataCollection: MongoCollection[Document] = mongoConnection.getMongoCollection("train-data")
  val groupCollection: MongoCollection[Document] = mongoConnection.getMongoCollection("userid-to-gender")

  def getUserIds(urlValue: String) = {
    val doc: FindObservable[Document] = userIdCollection.find(equal("_id", urlValue))
    val resp: Seq[Document] = Await.result(doc.toFuture(), Duration.Inf)
    val userIdListResp: Seq[BsonArray] = resp.map(doc => doc("userIds").asArray())

    var userIds = List[String]()

    if(userIdListResp.nonEmpty) {
      val userIdList: BsonArray = userIdListResp.head
      val iterator = userIdList.iterator()
      while(iterator.hasNext){

        userIds ::= (iterator.next().asString().getValue)
      }
    }
    userIds
  }

  def getGender(userId: String) = {
    val doc: FindObservable[Document] = trainDataCollection.find(equal("userid", userId))
    val resp = Await.result(doc.toFuture(), Duration.Inf)
    val userIdList: Seq[String] = resp.map(doc => doc("gender").asString().getValue)
    if(userIdList.nonEmpty) userIdList.head else "None"
  }


  def insertGroups(useridGroups: Seq[String]) = {
    val useridGroupJson = Json.obj("useridGroup" -> useridGroups).toString()
    val document = BsonDocument(useridGroupJson)
    val response = groupCollection.insertOne(document)

    response.toFuture().onComplete {
      case Success(value) => println(">>>>>>> successful insert")
      case _ => println(">>>>>>>failed ")
    }
  }
  

//  def getUserIdFromMongoDB(link: String) = {
//    val userIdList = getUserIds(link)
//    userIdList
//  }
}

