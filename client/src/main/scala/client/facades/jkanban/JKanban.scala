package client.facades.jkanban

import scala.scalajs.js
import scala.scalajs.js.UndefOr
import scala.scalajs.js.annotation.JSGlobal

//https://github.com/riktar/jkanban
@js.native
@JSGlobal(name = "jKanban")
class JKanban(val options: JKanbanOptions) extends js.Object {
	// add element in the board with ID boardID, element is the standart format
	def addElement(boardID: String, element: IJKanbanItem): Unit = js.native
}

trait JKanbanOptions extends js.Object{
	// Selector for kanban html element (example: html div with id = "myKanban" => element = "#myKanban")
	val element: String
	// 15px by default
	val gutter: js.UndefOr[String] = js.undefined
	// 250px by default
	val widthBoard: js.UndefOr[String] = js.undefined
	// kanban columns
	val boards: js.Array[JKanbanColumn]
	// if true boards can be draggable like items (default true)
	val dragBoards: js.UndefOr[Boolean] = js.undefined
	// add a button to board for easy item creation default ( false)
	val addItemButton: js.UndefOr[Boolean] = js.undefined
	// text or html content of the board button
	val buttonContent: js.UndefOr[String] = js.undefined
	// callback when any board's item are clicked
	val click: js.UndefOr[js.Function1[js.Object, _]] = js.undefined
//	dragEl          : function (el, source) {},                     // callback when any board's item are dragged
//	dragendEl       : function (el) {},                             // callback when any board's item stop drag
//	dropEl          : function (el, target, source, sibling) {},    // callback when any board's item drop in a board
//	dragBoard       : function (el, source) {},                     // callback when any board stop drag
//	dragendBoard    : function (el) {},                             // callback when any board stop drag
	// callback when the board's button is clicked
	val buttonClick: js.UndefOr[js.Function2[js.Object, String, _]] = js.undefined
}


// Column of kanban board
trait JKanbanColumn extends js.Object{
	// id of the column
	val id : String
	// title of the column
	val title : js.UndefOr[String] = js.undefined
	// css classes to add at the title "class1,class2,..."
	val `class`: js.UndefOr[String] = js.undefined
	// array of ids of boards where items can be dropped (default: [])
	val dragTo: js.UndefOr[js.Array[String]] = js.undefined
	// items of this column
	val item: js.Array[IJKanbanItem]
}

// item of kanban column
trait IJKanbanItem extends js.Object{
	val id: js.UndefOr[String] = js.undefined
	// content of the card
	val title: js.UndefOr[String] = js.undefined
}

class JKanbanItem(content: String) extends IJKanbanItem{
	override val title: UndefOr[String] = content
}