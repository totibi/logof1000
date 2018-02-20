package shared.cms.page

import shared.cms.message.{MessageContainer, MessageContainerInMemory}
import upickle.default.{macroRW, ReadWriter ⇒ RW}

case class Page(title: String){
	val messages: MessageContainer = new MessageContainerInMemory
}

object Page{
	implicit def rw: RW[Page] = macroRW
}
