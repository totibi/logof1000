package client.cms.page

import autowire._
import client.facades.tinymce.tinymce
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLElement
import shared.MainAPI
import shared.cms.message.Message
import shared.cms.page.Page

import scala.collection.mutable
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scalatags.JsDom.short._

object PageClient {
	def addMessageButton(page: Page, messageBlock: HTMLElement): HTMLElement = {
		val messageInput = textarea(*.id := "tinymce-textarea").render
		val addMessageButton =
			button(
				"new message",
				*.onclick := { (event: dom.Event) ⇒
					tinymce.triggerSave() // needed in ajax submit otherwise input.value will be empty
					client.Ajaxer[MainAPI].updatePage(page.cloneToAddMessage(Message(messageInput.value))).call().foreach { result ⇒
						messageBlock.innerHTML += messageInput.value
					}
				}
			).render
		val deleteButton =
			button(
				"new message",
				*.onclick := { (event: dom.Event) ⇒
					tinymce.triggerSave() // needed in ajax submit otherwise input.value will be empty
					client.Ajaxer[MainAPI].updatePage(page.cloneToAddMessage(Message(messageInput.value))).call().foreach { result ⇒
						messageBlock.innerHTML += messageInput.value
					}
				}
			).render
		div(
			*.float := "left",
			messageInput, addMessageButton
		).render
	}

	def page2Element(page: Page): HTMLElement = {
		val messageBlock = div(*.float := "right").render
		clearComponent(messageBlock)
		page.messages.foreach(message ⇒ messageBlock.innerHTML += message.content)
		div(
			script(
				// TODO вынести команду удаления редакторов в скала код (необходим всвязи с тем, что элементы появляются динамически, иногда через ajax)
				"""
			 tinymce.EditorManager.execCommand('mceRemoveEditor',true, 'tinymce-textarea');
			 tinymce.init({
					 selector: '#tinymce-textarea',
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
			),
			h1("Hello this is " + page.title),
			addMessageButton(page, messageBlock),
			messageBlock
		).render
	}

	def pageKanban(root: HTMLElement, page: Page): Unit = {
		import scala.scalajs.js.Dynamic.{global ⇒ g, newInstance ⇒ jsnew}
		import scala.scalajs.js
		val kanbanHtmlDiv = div(*.id := "myKanban").render
		root.appendChild(kanbanHtmlDiv)
		val kanban = jsnew(g.jKanban)(js.Dynamic.literal(
			element = "#myKanban",
			gutter = "10px",
			widthBoard = "450px",
			addItemButton = true,
			buttonClick = (el: js.Object, boardId: js.Object) ⇒ {
				val formItem = form(
					div(
						*.`class` := "form-group",
						textarea(*.`class` := "form-control", *.rows := "2", *.autofocus := true)
					),
					div(
						*.`class` := "form-group",
						button(*.`type` := "submit", *.`class` := "btn btn-primary btn-xs pull-right", "Submit"),
						button(*.`type` := "button", *.id := "CancelBtn", *.`class` := "btn btn-default btn-xs pull-right", "Cancel")
					),
					*.`class` := "itemform"
				)
//				kanban
//					addForm(boardId, formItem)
			},
			boards = js.Array(
				js.Dynamic.literal(
					id = "wtf",
					title = "just die",
					`class` = "info,good"
				)
			)
			//			"click" →
		))
		kanban
	}

	def clearComponent(component: HTMLElement): Unit =
		while (component.firstChild != null) {
			component.removeChild(component.firstChild)
		}

}
