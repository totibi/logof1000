package client.cms.view.message

import org.scalajs.dom.raw.HTMLElement
import scalatags.JsDom.short._
import shared.cms.message.Message


object MessageView {

	// adding message content to messages container
	def addMessageContent(container: HTMLElement, message: Message):Unit = {
		val messageWrapper = div(*.`class` := "messageCard").render
		val messageContent = div(*.`class` := "messageContent").render
		messageContent.innerHTML = message.content
		messageWrapper.appendChild(messageContent)
		container.appendChild(messageWrapper)
	}

}
