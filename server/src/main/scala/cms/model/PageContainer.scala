package cms.model

import java.util.concurrent.TimeUnit

import org.mongodb.scala.MongoCollection
import server.model.ServerDBC
import shared.cms.message.Message
import shared.cms.page.Page

import scala.concurrent.Await
import scala.concurrent.duration.Duration

// TODO проблема с mongoid, точнее их нет в классах, приходится работать по titles
abstract class PageContainer {
	def getPages: Seq[Page]

	def addPage(pageToAdd: Page): Unit

	def updatePage(pageToUpdate: Page): Unit

	def deletePage(pageToDelete: Page): Unit
}

object PageContainerInDB extends PageContainer {

	import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
	import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
	import org.mongodb.scala.bson.codecs.Macros._

	import org.mongodb.scala.model.{Updates, Filters}

	private val pagesRegistry = fromRegistries(fromProviders(classOf[Page], classOf[Message]), DEFAULT_CODEC_REGISTRY)

	val collectionName = "PagesCollection"
	val pagesCollection: MongoCollection[Page] = ServerDBC.getDB(pagesRegistry).getCollection(collectionName)

	override def getPages: Seq[Page] = Await.result(pagesCollection.find().toFuture(), Duration(10, TimeUnit.SECONDS))

	override def addPage(pageToAdd: Page): Unit = Await.result(pagesCollection.insertOne(pageToAdd).toFuture(), Duration(10, TimeUnit.SECONDS))

	override def updatePage(pageToUpdate: Page): Unit =
		Await.result(
			pagesCollection.updateOne(Filters.equal("title", pageToUpdate.title),Updates.set("messages", pageToUpdate.messages)).toFuture(),
			Duration(10, TimeUnit.SECONDS)
		)

	override def deletePage(pageToDelete: Page): Unit = Await.result(pagesCollection.deleteOne(Filters.equal("title", pageToDelete.title)).toFuture(), Duration(10, TimeUnit.SECONDS))
}