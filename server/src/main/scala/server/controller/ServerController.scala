package server.controller

import java.net.URL

import cms.model.{PageContainer, PageContainerInDB}
import rss.RssReader
import shared.MainAPI
import shared.cms.page.Page
import shared.rss.RssFeed

trait ServerController extends MainAPI {
	val pageContainer: PageContainer = PageContainerInDB

	override def updatePage(updatedPage: Page): Page = {
		pageContainer.updatePage(updatedPage)
		updatedPage
	}

	override def addPage(newPage: Page): Page = {
		pageContainer.addPage(newPage)
		newPage
	}

	override def getPages(emptyDontWork: Boolean = true): Seq[Page] = {
		pageContainer.getPages
	}

	override def fetchFeeds(url: String): Seq[RssFeed] = {
		RssReader.read(new URL(url))
	}
}
