package cms.model

import shared.cms.page.Page

abstract class PageContainer {
	def getPages: Seq[Page]
	def addPage(pageToAdd: Page): Unit
}


// used for testing
object PageContainerInMemory extends PageContainer{
	private[this] var pagesInMemory: Seq[Page] = List(Page("Kek"), Page("New page1"), Page("New page2"))

	override def getPages: Seq[Page] = pagesInMemory

	override def addPage(pageForAdd: Page): Unit = {
		pagesInMemory = pagesInMemory :+ pageForAdd
	}
}