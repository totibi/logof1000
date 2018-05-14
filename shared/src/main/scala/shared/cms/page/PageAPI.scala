package shared.cms.page

trait PageAPI{
	def addPage(newPage: Page): Page
	def getPages(emptyDontWork: Boolean = true): Seq[Page]
	def updatePage(updatedPage: Page): Page
	def deletePage(pageToDelete: Page): Page
}

