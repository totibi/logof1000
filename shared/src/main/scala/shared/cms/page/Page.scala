package shared.cms.page

import shared.cms.message.Message
import shared.cms.page.kanban.Kanban
import upickle.default.{macroRW, ReadWriter ⇒ RW}

case class Page(title: String, messages: Seq[Message], kanban: Kanban = Kanban()){
	// TODO look why i do this, look for better
	def cloneToAddMessage(newMessage: Message) = Page(title, messages :+ newMessage, kanban)
	def cloneWithNewMessages(newMessages: Seq[Message]) = Page(title, newMessages, kanban)
	def cloneToRemoveMessage(messageToRemove: Message) = Page(title, messages.filterNot(_.content == messageToRemove.content), kanban) // TODO headshot
	def cloneToChangeKanban(newKanban: Kanban) = Page(title, messages, newKanban) // TODO headshot
}

object Page{
	implicit def rw: RW[Page] = macroRW
}
