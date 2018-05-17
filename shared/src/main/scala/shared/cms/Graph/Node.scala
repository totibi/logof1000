package shared.cms.Graph
import upickle.default.{macroRW, ReadWriter ⇒ RW}

case class Node(id: String, label: String) {

}
object Node {
	implicit def rw: RW[Node] = macroRW
}