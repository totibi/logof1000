package shared.cms.page.kanban
import upickle.default.{macroRW, ReadWriter ⇒ RW}

case class Kanban(columns: Seq[KanbanColumn] = Nil) {

}

object Kanban{
	implicit def rw: RW[Kanban] = macroRW
}
