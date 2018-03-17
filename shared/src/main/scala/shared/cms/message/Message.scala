package shared.cms.message
import upickle.default.{macroRW, ReadWriter ⇒ RW}

case class Message(content: String)

object Message{
	implicit def rw: RW[Message] = macroRW
}
