package client.cms.view.message

import org.scalajs.dom.raw.HTMLElement
import shared.cms.message.Message
import scalatags.JsDom.short._


object MessageView {

	def getMessageCard(message: ⇒ Message):HTMLElement = {
		val messageContainer = div(*.width := "400px").render
		messageContainer.innerHTML = message.content
		messageContainer
	}

}
