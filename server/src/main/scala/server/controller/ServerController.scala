package server.controller

import java.net.URL

import cms.model.{PageContainer, PageContainerInMemory}
import rss.RssReader
import shared.MainAPI
import shared.cms.message.Message
import shared.cms.page.Page
import shared.rss.RssFeed

trait ServerController extends MainAPI {
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

	override def fetchFeeds(url: String): Seq[RssFeed] = {
		RssReader.read(new URL(url))
	}
}
