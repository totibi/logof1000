package shared.cms.page

import shared.cms.message.Message
import upickle.default.{macroRW, ReadWriter ⇒ RW}

case class Page(title: String, messages: Seq[Message]){
	// TODO look why i do this, look for better
	def cloneToAddMessage(newMessage: Message) = Page(title, messages ++ Seq(newMessage))
	def cloneWithNewMessages(newMessages: Seq[Message]) = Page(title, newMessages)
}

object Page{
	implicit def rw: RW[Page] = macroRW
}
