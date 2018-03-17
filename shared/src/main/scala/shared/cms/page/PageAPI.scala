package shared.cms.page

import shared.cms.message.Message

trait PageAPI{
	def addPage(newPage: Page): Page
	def getPages(emptyDontWork: Boolean = true): Seq[Page]
	def updatePage(updatedPage: Page): Page
}

