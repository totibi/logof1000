package shared.cms.page

import shared.cms.page.kanban.KanbanAPI

trait PageAPI extends KanbanAPI{
	def addPage(newPage: Page): Page
	// TODO emptydontwork
	def getPages(emptyDontWork: Boolean = true): Seq[Page]
	def updatePage(updatedPage: Page): Page
	def deletePage(pageToDelete: Page): Page
}

