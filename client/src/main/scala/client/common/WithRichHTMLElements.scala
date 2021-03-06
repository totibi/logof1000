package client.common

import client.cms.view.message.MessageView
import client.cms.view.page.PageView
import client.facades.jkanban._
import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.{Element, Node}
import shared.cms.message.Message
import shared.cms.page.Page
import shared.cms.page.kanban.{Kanban, KanbanColumn, KanbanItem}

import scala.scalajs.js
import scala.scalajs.js.UndefOr

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

	// TODO remove this layer? Jkanban - kanban you have upickle but jkanban is view
	// for transfer client Kanban data to server
	implicit class RichClientJKanban(clientKanban: JKanban) {

		def toServerData: Kanban = {
			Kanban(columnsToServerColumns())
		}

		private def columnsToServerColumns(): Seq[KanbanColumn] = {
			clientKanban.options.boards.map(jColumn ⇒ {
				KanbanColumn(jColumn.title.getOrElse("undefined"), itemsToServerItems(jColumn))
			})
		}

		private def itemsToServerItems(jColumn: JKanbanColumn): Seq[KanbanItem] = {
			jColumn.item.map(jItem ⇒ KanbanItem(jItem.title.getOrElse("undefined")))
		}

	}

	// for transfer server data to client
	implicit class KanbanToClient(serverKanban: Kanban) {

		import scala.scalajs.js
		import js.JSConverters._

		def toClientKanbanOptions: JKanbanOptions = {
			val options = new JKanbanOptions {
				override val element: String = "#myKanban"
				override val boards: js.Array[JKanbanColumn] = getClientsColumns().toJSArray
			}
			options
		}

		private def getClientsColumns(): Seq[JKanbanColumn] = {
			serverKanban.columns.map(_.toClientColumn)
		}

		private def columnItemsToServerItems(kanbanColumn: KanbanColumn): Seq[IJKanbanItem] = {
			kanbanColumn.items.map(item ⇒ new JKanbanItem(content = item.content))
		}

	}

	implicit class RichKanbanColumn(kanbanColumn: KanbanColumn) {

		import js.JSConverters._

		def additionalStringForId = "-kanbanColumn"

		def getClientId = s"${kanbanColumn.title}$additionalStringForId"

		def toClientColumn: JKanbanColumn = {
			new JKanbanColumn {
				override val id: String = getClientId
				override val title: UndefOr[String] = kanbanColumn.title
				override val item: js.Array[IJKanbanItem] = columnItemsToServerItems().toJSArray
			}
		}


		private def columnItemsToServerItems(): Seq[IJKanbanItem] = {
			kanbanColumn.items.map(item ⇒ new JKanbanItem(content = item.content))
		}
	}

}
