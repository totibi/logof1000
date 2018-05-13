package client.cms.view.message

import client.common.WithRichHTMLElements
import org.scalajs.dom.raw.HTMLElement
import shared.cms.message.Message

// TODO look for better they adding api to implicit class or should it be another impl class on one biggest, DI
// implicit message specific methods for html elements
trait WithRichElementForMessage {
	self: WithRichHTMLElements ⇒

	implicit class RichHTMLElementForMessage(val currentElem: HTMLElement) {

		// TODO fluent decorator
		// Adding message content to current element
		def appendMessage(message: Message): HTMLElement = {
			MessageView.addMessageContent(currentElem, message)
			currentElem
		}

		// TODO fluent decorator
		//		 Adding message content to current element
		def appendMessages(messages: Seq[Message]): HTMLElement = {
			messages foreach currentElem.appendMessage
			currentElem
		}

	}

}