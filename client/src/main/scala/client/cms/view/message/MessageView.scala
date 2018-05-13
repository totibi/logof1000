package client.cms.view.message

import autowire._
import client.Ajaxer
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLElement
import scalatags.JsDom.short._
import shared.MainAPI
import shared.cms.message.Message
import shared.cms.page.Page


object MessageView {

	// adding message content to messages container
	def addMessageContent(container: HTMLElement, page: Page, message: Message): Unit = {
		val messageWrapper = div(*.`class` := "messageCard").render
		val messageContent = div(*.`class` := "messageContent").render
		messageContent.innerHTML = message.content
		messageWrapper.appendChildren(messageContent, getDeleteMsgButton(page, message, messageWrapper))
		container.appendChild(messageWrapper)
	}

	def getDeleteMsgButton(page: Page, message: Message, messageWrapper: HTMLElement): HTMLElement = {
		val deleteBtn = button(
			"delete",
			*.onclick := { (event: dom.Event) ⇒ {
				Ajaxer[MainAPI].updatePage(page.cloneToRemoveMessage(message)).call().foreach(
					newPage ⇒ {
						messageWrapper.parentNode.removeChild(messageWrapper)
					}
				)
			}}
		).render
		deleteBtn
	}


}
