package shared.cms.page.kanban

import upickle.default.{macroRW, ReadWriter ⇒ RW}

case class KanbanItem(content: String)

object KanbanItem{
	implicit def rw: RW[KanbanItem] = macroRW
}