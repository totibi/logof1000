package com.memories.logof1000.cms.page

import com.memories.logof1000.shared.cms.page.Page
import org.scalajs.dom.ext.Ajax

// страницы добавляются через этот контроллер
object PageController {

	def addPageRequest(newPage: ⇒Page): Unit = {
		Ajax.post("/addPage", upickle.default.write[Page](newPage))
	}

}
