package client.common

import org.scalajs.dom.Element
import org.scalajs.dom.raw.HTMLElement

trait WithRichHTMLElements {

	implicit class RichHTMLElement(currentElem: HTMLElement) {

		// remove filtered childs and return them as seq of elements
		def removeNodesFromElement(filter: (Element) ⇒ Boolean): Seq[Element] = {
			var deletedNodes: Seq[Element] = Nil
			if (currentElem.hasChildNodes()) {
				def childs = currentElem.children

				for (id ← 0 until childs.length) {
					val currentChild = childs(id)
					if (filter(currentChild)) {
						println(s"before $childs")
						currentElem.removeChild(currentChild)
						println(s"after $childs")
						deletedNodes :+= currentChild
					}
				}
			}
			deletedNodes
		}

		// removing all childs
		def clearFromChilds(): Unit = {
			while (currentElem.firstChild != null) {
				currentElem.removeChild(currentElem.firstChild)
			}
		}
	}

}
