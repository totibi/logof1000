package shared.cms.Graph
import upickle.default.{macroRW, ReadWriter ⇒ RW}

case class Edge(fromId: String, toId: String, label: String, id: String) {

}
object Edge{
	implicit def rw: RW[Edge] = macroRW
}
