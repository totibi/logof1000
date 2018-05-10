package client.facades.tinymce

import org.scalajs.dom.raw.HTMLElement
import scalatags.JsDom.short.script

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@JSGlobal
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

object TinyMCEScala {
	// TODO use scalajs
	// script for init tinymce in page (btw work with ajax throught removing old exemplars)
	def initScript(selectorId: ⇒ String): String = {
		// TODO вынести команду удаления редакторов в скала код (необходим всвязи с тем, что элементы появляются динамически, иногда через ajax)
		s"""
			 tinymce.EditorManager.execCommand('mceRemoveEditor',true, 'tinymce-textarea');
			 tinymce.init({
					 selector: '#$selectorId',
					  plugins: [
					 |    'advlist autolink lists link image charmap print preview anchor textcolor',
					 |    'searchreplace visualblocks code fullscreen save directionality emoticons ',
					 |    'insertdatetime media table contextmenu paste code help wordcount'
					 |  ],
					 menubar: 'file edit insert view format table tools help',
					 toolbar: 'fullscreen | insertfile undo redo | styleselect fontsizeselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image | print preview media fullpage | forecolor backcolor emoticons',
					 nowrap: true,
					 paste_as_text: true,
					 paste_data_images: true,
					 content_css: [
					 |    '//fonts.googleapis.com/css?family=Lato:300,300i,400,400i',
					 |    '//www.tinymce.com/css/codepen.min.css'
					 |    ]
					 });
				""".stripMargin
	}
}