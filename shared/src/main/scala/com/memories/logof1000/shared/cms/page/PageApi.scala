package com.memories.logof1000.shared.cms.page

trait PageApi {
	def addPage(newPage: Page): Page
	def getPages: Seq[Page]
}

