package server.model

import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.{MongoClient, MongoDatabase}

// server data base connection
object ServerDBC {
	val dbName = "JustMongo"
	// pool of connections
	val mongoClient = MongoClient()

	def getDB: MongoDatabase = mongoClient.getDatabase(dbName)
	def getDB(codecRegistry: CodecRegistry): MongoDatabase = mongoClient.getDatabase(dbName).withCodecRegistry(codecRegistry)
}
