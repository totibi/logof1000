package server

import java.net.URL

import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
import server.cms.{PageContainer, PageContainerInMemory}
import server.rss.RssReader
import shared.MainAPI
import shared.cms.message.Message
import shared.cms.page.Page
import shared.rss.RssFeed

trait ServerController extends MainAPI{
	val pageContainer: PageContainer = PageContainerInMemory

	override def addPage(newPage: Page): Page = {
		pageContainer.addPage(newPage)
		newPage
	}

	override def getPages(emptyDontWork: Boolean = true): Seq[Page] = {
		pageContainer.getPages
	}

	override def addMessageToPage(message: Message, page: Page): Boolean = {
		page.messages.addMessage(message)
		true
	}

	implicit val system = ActorSystem("server-system")
	implicit val materializer = ActorMaterializer()

	override def fetchFeeds(url: String): Seq[RssFeed] = {
		system.actorOf(Props[RssReader]) ! new URL(url)
		Nil
	}
}
