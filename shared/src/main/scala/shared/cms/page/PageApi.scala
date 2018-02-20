package shared.cms.page

trait PageApi {
	def addPage(newPage: Page): Page
	def getPages(emptyDontWork: Boolean = true): Seq[Page]
}

