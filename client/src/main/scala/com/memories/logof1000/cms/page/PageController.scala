package com.memories.logof1000.cms.page

import com.memories.logof1000.shared.cms.page.Page
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import scala.scalajs.concurrent.JSExecutionContext.queue

// страницы добавляются через этот контроллер
object PageController {

	def addPageRequest(newPage: ⇒Page)(function: dom.XMLHttpRequest ⇒ Unit) = {
		Ajax.post("/addPage", upickle.default.write[Page](newPage)).foreach(function)
	}

}
