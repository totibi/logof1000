package shared.cms.page

import shared.cms.message.Message
import upickle.default.{macroRW, ReadWriter ⇒ RW}

case class Page(title: String, messages: Seq[Message]){
	// TODO look why i do this, look for better
	def cloneToAddMessage(newMessage: Message) = Page(title, messages :+ newMessage)
	def cloneWithNewMessages(newMessages: Seq[Message]) = Page(title, newMessages)
	def cloneToRemoveMessage(messageToRemove: Message) = Page(title, messages.filterNot(_.content == messageToRemove.content)) // TODO headshot
}

object Page{
	implicit def rw: RW[Page] = macroRW
}
