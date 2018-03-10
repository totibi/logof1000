package client.tinymce

import scala.scalajs.js

@js.native
object tinymce extends js.Object {
	// needed in ajax submit otherwise input.value will be empty
	def triggerSave(): Unit = js.native
//	var EditorManager: EditorManager = js.native
}
//
//@js.native
//class EditorManager extends js.Object{
//	var editors: js.Array[Editor] = js.native
//}
//
//@js.native
//trait Editor extends js.Object