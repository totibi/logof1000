package shared.cms.page.kanban
import upickle.default.{macroRW, ReadWriter ⇒ RW}
case class KanbanColumn(title: String, items: Seq[KanbanItem] = Nil) {

}
object KanbanColumn{
	implicit def rw: RW[KanbanColumn] = macroRW
}