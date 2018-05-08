package client.cms.view.page

import autowire._
import client.cms.view.message.MessageView
import client.facades.jkanban.{JKanban, JKanbanColumn, JKanbanItem, JKanbanOptions}
import client.facades.tinymce.{TinyMCEScala, tinymce}
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLElement
import scalatags.JsDom.short._
import shared.MainAPI
import shared.cms.message.Message
import shared.cms.page.Page

import scala.scalajs.js.UndefOr

object PageView {
	def getNewMessageArea(page: Page, messageBlock: HTMLElement): HTMLElement = {
		val tinymceAreaId = "tinymce-textarea"
		val messageInput = textarea(*.id := tinymceAreaId).render
		val addMessageButton =
			button(
				"save new message",
				*.onclick := { (event: dom.Event) ⇒
					tinymce.triggerSave() // needed in ajax submit otherwise input.value will be empty
					client.Ajaxer[MainAPI].updatePage(page.cloneToAddMessage(Message(messageInput.value))).call().foreach { result ⇒
						// TODO obserable pattern for removing this dependencie
						messageBlock.innerHTML += messageInput.value
					}
				}
			).render
		div(
			script(TinyMCEScala.initScript(tinymceAreaId)),
			messageInput, addMessageButton
		).render
	}

	// page for current selected page content
	def page2Element(page: Page): HTMLElement = {
		val messagesBlock = div.render
		clearComponent(messagesBlock)
		// render existed messages
		page.messages.foreach(message ⇒ messagesBlock.appendChild(MessageView.getMessageCard(message)))
		div(
			h1("Hello this is " + page.title),
			getNewMessageArea(page, messagesBlock),
			messagesBlock
		).render
	}

	// kanban for selected page
	def pageKanban(root: HTMLElement, page: Page): Unit = {
		import scala.scalajs.js
		val kanbanHtmlDiv = div(*.id := "myKanban").render
		root.appendChild(kanbanHtmlDiv)
		val options = new JKanbanOptions {
			override val element: String = "#myKanban"
			override val boards: js.Array[JKanbanColumn] =  js.Array(
				new JKanbanColumn {
					override val id: String = "first"
					override val item: js.Array[JKanbanItem] = js.Array(new JKanbanItem {
						override val id: String = "kek"
						override val title: UndefOr[String] = "whatefuck"
					})
				}
			)
		}
		scala.scalajs.js.special.debugger()
		val kanban = new JKanban(options)
		kanban
	}

	def clearComponent(component: HTMLElement): Unit =
		while (component.firstChild != null) {
			component.removeChild(component.firstChild)
		}

}
