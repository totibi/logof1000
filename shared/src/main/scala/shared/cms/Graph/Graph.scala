package shared.cms.Graph
import upickle.default.{macroRW, ReadWriter ⇒ RW}

case class Graph(title: String = "default", nodes: Seq[Node], edges: Seq[Edge]) {

}
object Graph{
	implicit def rw: RW[Graph] = macroRW
}