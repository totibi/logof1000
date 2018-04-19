package client.facades.jkanban

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSGlobal, ScalaJSDefined}

@js.native
@JSGlobal(name = "jKanban")
class JKanban(options: JKanbanOptions) extends js.Object {

}

@ScalaJSDefined
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
//	buttonClick     : function(el, boardId) {}                      // callback when the board's button is clicked
}


// Column of kanban board
@ScalaJSDefined
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
	val item: js.Array[JKanbanItem]
}

// item of kanban column
@ScalaJSDefined
trait JKanbanItem extends js.Object{
	val id: String
	// content of the card
	val title: js.UndefOr[String] = js.undefined
}
