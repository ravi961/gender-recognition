package dataaccess
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase}

class MongoDB {

//  val uri: String = "mongodb+srv://ravi:ravi123@nano0-v001x.mongodb.net/gender-recognition?retryWrites=true&w=majority"
  val uri: String = "mongodb://localhost:27017/gender-recognition"
  System.setProperty("org.mongodb.async.type", "netty")
  val client: MongoClient = MongoClient(uri)
  val db: MongoDatabase = client.getDatabase("gender-recognition")

  def getMongoCollection(collectionName: String)={
        db.getCollection(collectionName)
      }

}


