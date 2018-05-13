package client.common

import client.cms.view.message.MessageView
import client.cms.view.page.PageView
import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.{Element, Node}
import shared.cms.message.Message
import shared.cms.page.Page

trait WithRichHTMLElements
//	extends WithRichElementForPage with WithRichElementForMessage  TODO Upickle broken
 {

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

		// TODO fluent decorator
		// Adding page html elements to current html element
		def appendPage(page: Page): HTMLElement = {
			PageView.buildPageContent(currentElem, page)
			currentElem
		}

		// TODO fluent decorator
		// Adding message content to current element
		def appendMessage(page: Page, message: Message): HTMLElement = {
			MessageView.addMessageContent(currentElem, page, message)
			currentElem
		}

		// TODO fluent decorator
		//		 Adding message content to current element
		def appendMessages(page: Page): HTMLElement = {
			// TODO i dont like this one (kick page with message for back update cake)
			page.messages foreach (message ⇒ currentElem.appendMessage(page, message))
			currentElem
		}

	}

}
