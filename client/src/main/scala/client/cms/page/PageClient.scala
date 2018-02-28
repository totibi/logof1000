package client.cms.page

import autowire._
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLElement
import shared.MainAPI
import shared.cms.message.Message
import shared.cms.page.Page

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scalatags.JsDom.short._

object PageClient {
	def addMessageButton(page: Page): HTMLElement = {
		val messageInput = textarea(*.id := "tinyMCE").render
		val addMessageButton =
			button(
				"new message",
				*.onclick := { (event: dom.Event) ⇒
					val newMessage = Message(messageInput.value)
					client.Ajaxer[MainAPI].addMessageToPage(newMessage, page).call().foreach { result ⇒
						if (result) {
							page.messages.addMessage(newMessage)
							messageBlock.appendChild(p(newMessage.content).render)
						}
					}
				}
			).render
		div(
			*.float := "left",
			*.width := "100%",
			*.height := "100%",
			messageInput, addMessageButton
		).render
	}

	def messageBlock: HTMLElement = div(*.float := "right").render

	def page2Element(page: Page): HTMLElement = {
		clearComponent(messageBlock)
		page.messages.getMessages.foreach(message ⇒ messageBlock.appendChild(p(message.content).render))
		div(
			*.width := "1000px",
			*.height := "700px",
			script(
				"""
			 tinymce.init({
					 selector: '#tinyMCE',
					  plugins: [
					 |    'advlist autolink lists link image charmap print preview anchor textcolor',
					 |    'searchreplace visualblocks code fullscreen',
					 |    'insertdatetime media table contextmenu paste code help wordcount'
					 |  ],
					 menubar: 'edit view',
					 toolbar: 'paste fullscreen',
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
			addMessageButton(page),
			messageBlock
		).render
	}

	def clearComponent(component: HTMLElement): Unit =
		while (component.firstChild != null){
			component.removeChild(component.firstChild)
		}

}
