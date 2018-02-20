package client.cms.page

import autowire._
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLElement
import shared.cms.message.Message
import shared.cms.page.{Page, PageApi}

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
					client.Ajaxer[PageApi].addMessageToPage(newMessage, page).call().foreach { result ⇒
						if (result) {
							page.messages.addMessage(newMessage)
							messageBlock.appendChild(p(newMessage.content).render)
						}
					}
				}
			).render
		div(
			*.float := "left",
			messageInput, addMessageButton
		).render
	}

	val messageBlock: HTMLElement = div(*.float := "right").render

	def page2Element(page: Page): HTMLElement = {
		clearComponent(messageBlock)
		page.messages.getMessages.foreach(message ⇒ messageBlock.appendChild(p(message.content).render))
		div(
			script("tinymce.init({\n  " +
						 "selector: '#tinyMCE',\n  " +
						 "plugin: 'a_tinymce_plugin',\n  " +
						 "a_plugin_option: true,\n  " +
						 "a_configuration_option: 400\n" +
						 "});"),
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
