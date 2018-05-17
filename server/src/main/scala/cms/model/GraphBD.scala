package cms.model

import java.util.concurrent.TimeUnit

import org.mongodb.scala.MongoCollection
import server.model.ServerDBC
import shared.cms.Graph.{Edge, Graph, Node}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object GraphDB {
	import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
	import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
	import org.mongodb.scala.bson.codecs.Macros._
	private val graphRegistry = fromRegistries(fromProviders(classOf[Node], classOf[Edge], classOf[Graph]), DEFAULT_CODEC_REGISTRY)

	val collectionName = "GraphCollection"
	val graphCollection: MongoCollection[Graph] = ServerDBC.getDB(graphRegistry).getCollection(collectionName)

	def getGraph: Seq[Graph] = Await.result(graphCollection.find().toFuture(), Duration(10, TimeUnit.SECONDS))

	def saveGraph(graphToAdd: Graph): Unit = Await.result(graphCollection.insertOne(graphToAdd).toFuture(), Duration(10, TimeUnit.SECONDS))

//	def updatePage(graphToUpdate: Graph): Unit =
//		Await.result(
//			graphCollection.updateOne(Filters.equal("title", graphToUpdate.title),Updates.set("edges", graphToUpdate.edges)).toFuture(),
//			Duration(10, TimeUnit.SECONDS)
//		)

}
