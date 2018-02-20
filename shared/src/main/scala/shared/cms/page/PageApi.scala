package shared.cms.page

import shared.cms.message.Message

trait PageApi {
	def addPage(newPage: Page): Page
	def getPages(emptyDontWork: Boolean = true): Seq[Page]
	def addMessageToPage(message: Message, page: Page): Boolean
}

