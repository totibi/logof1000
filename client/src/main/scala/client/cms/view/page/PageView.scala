package client.cms.view.page

import autowire._
import client.Ajaxer
import client.facades.jkanban._
import client.facades.tinymce.{TinyMCEScala, tinymce}
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLElement
import scalatags.JsDom.short._
import shared.MainAPI
import shared.cms.message.Message
import shared.cms.page.Page

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
						// TODO obserable pattern for removing this(adding message to page by this code and updating button) dependencie
						val oldButton = dom.document.getElementById(getPageMenuBtnClass(page))
						messageBlock.appendChild({
							val kek = div().render
							val wtf = div().render
							kek.innerHTML = messageInput.value
							wtf.appendChild(kek)
							wtf
						})
					}
				}
			).render
		div(
			script(TinyMCEScala.initScript(tinymceAreaId)),
			messageInput, addMessageButton
		).render
	}

	def getPageMenuItemId(page: Page): String = {
		s"${page.title}-menuItem"
	}

	def getPageMenuBtnClass(page: Page): String = {
		s"${page.title}-menuBtn"
	}

	// fill container by page content
	def buildPageContent(container: HTMLElement, page: Page): Unit = {
		val messagesBlock = div(*.`class` := "messagesBlock").render
		messagesBlock.appendMessages(page)
		container.appendChildren(
			h1("Hello this is " + page.title).render,
			getNewMessageArea(page, messagesBlock),
			messagesBlock
		)
	}

	// kanban for selected page
	def pageKanban(container: HTMLElement, page: Page): Unit = {
		val kanbanHtmlDiv = div(*.id := "myKanban").render
		container.appendChild(kanbanHtmlDiv)
		// TODO this will init kanban to work with kanbanHtmlDiv
		val kanban = new JKanban(page.kanban.toClientKanbanOptions)
		// TODO refactoring
		val taskTextInput = input.render
		val addTaskBtn =
			button(
				"create task",
				*.onclick := { (event: dom.Event) ⇒
					kanban.addElement(page.kanban.columns.head.getClientId, new JKanbanItem(taskTextInput.value))
					Ajaxer[MainAPI].updatePage(page.cloneToChangeKanban(kanban.toServerData)).call().foreach{
						wtfpage ⇒ println(wtfpage)
					}
				}
			).render
		container.appendChildren(taskTextInput, addTaskBtn)
	}

}
