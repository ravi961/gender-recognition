package services

import dataaccess.MongoDB
import javax.inject.Inject
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.{Completed, Document, FindObservable, MongoCollection, Observer}

import scala.concurrent.Await
import scala.concurrent.duration.Duration


class MongoStorageService @Inject()(mongoConnection: MongoDB) {

  def getUserIds(urlValue: String) = {
    val userIdCollection: MongoCollection[Document] = mongoConnection.getMongoCollection("url-to-userId")
    val doc: FindObservable[Document] = userIdCollection.find(equal("url", urlValue))

    val resp = Await.result(doc.toFuture(), Duration.Inf)
    val userIdList: Seq[String] = resp.map(doc => doc("userid").asString().getValue)
    userIdList

  }

//  def getUserIdFromMongoDB(link: String) = {
//    val userIdList = getUserIds(link)
//    userIdList
//  }
}

