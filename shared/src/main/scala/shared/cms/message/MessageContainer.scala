package shared.cms.message

abstract class MessageContainer {
	def getMessages: Seq[Message]
	def addMessage(pageToAdd: Message): Unit
}

class MessageContainerInMemory extends MessageContainer{
	private[this] var messagesInMemory: Seq[Message] = Nil

	override def getMessages: Seq[Message] = messagesInMemory

	override def addMessage(pageForAdd: Message): Unit = {
		messagesInMemory = messagesInMemory :+ pageForAdd
	}
}