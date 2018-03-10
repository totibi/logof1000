package client.cms.page

import autowire._
import client.tinymce.tinymce
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLElement
import shared.MainAPI
import shared.cms.message.Message
import shared.cms.page.Page

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
					val newMessage = Message(messageInput.value)
					client.Ajaxer[MainAPI].addMessageToPage(newMessage, page).call().foreach { result ⇒
						if (result) {
							page.messages.addMessage(newMessage)
							messageBlock.innerHTML += newMessage.content
						}
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
		page.messages.getMessages.foreach(message ⇒ messageBlock.innerHTML += message.content)
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

	def clearComponent(component: HTMLElement): Unit =
		while (component.firstChild != null){
			component.removeChild(component.firstChild)
		}

}
