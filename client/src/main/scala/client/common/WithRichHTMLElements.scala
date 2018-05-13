package client.common

import client.cms.view.message.WithRichElementForMessage
import client.cms.view.page.WithRichElementForPage
import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.{Element, Node}

trait WithRichHTMLElements extends WithRichElementForPage with WithRichElementForMessage{

	// TODO val for ApiForPage using
	// TODO look for decorator which allow using all methods like fluent api (returning initial element for next api invokes) (look TODO fluent decorator)
	// extended html element api in most cases return same element for fluent api
	implicit class RichHTMLElement(val currentElem: HTMLElement) {

		// remove filtered childs and return them as seq of elements
		def removeNodesFromElement(filter: (Element) ⇒ Boolean): Seq[Element] = {
			var deletedNodes: Seq[Element] = Nil
			if (currentElem.hasChildNodes()) {
				def children = currentElem.children

				for (id ← 0 until children.length) {
					val currentChild = children(id)
					if (filter(currentChild)) {
						currentElem.removeChild(currentChild)
						deletedNodes :+= currentChild
					}
				}
			}
			deletedNodes
		}

		// TODO fluent decorator
		// append all nodes in parameter to current element through appendChild
		def appendChildren(nodes: Node*): HTMLElement = {
			nodes foreach currentElem.appendChild
			currentElem
		}

		// TODO fluent decorator
		// removing all children and returning current element
		def clearFromChildren(): HTMLElement = {
			while (currentElem.firstChild != null) {
				currentElem.removeChild(currentElem.firstChild)
			}
			currentElem
		}
	}

}
