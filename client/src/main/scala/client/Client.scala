package client

import autowire._
import client.Client.pagesElem
import client.cms.view.page.PageClient
import client.rss.RssClient
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLElement
import shared.MainAPI
import shared.cms.page.Page
import upickle.default

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scalatags.JsDom.short._

object Ajaxer extends autowire.Client[String, upickle.default.Reader, upickle.default.Writer] {
	override def doCall(req: Request): Future[String] = {
		dom.ext.Ajax.post(
			url = "/api/" + req.path.mkString("/"),
			data = upickle.default.write(req.args)
		).map(response ⇒ {
			response.responseText
		})
	}

	override def read[Result](p: String)(implicit evidence$1: default.Reader[Result]): Result = upickle.default.read[Result](p)

	override def write[Result](r: Result)(implicit evidence$2: default.Writer[Result]): String = upickle.default.write(r)
}

@JSExportTopLevel("Client")
object Client {

	val menuContainer: HTMLElement = div().render
	// Отоброжает содержимое выбранной страницы
	val messagesContainer: HTMLElement = div(*.float := "left").render

	@JSExport
	def main(): Unit = {
		val pageTitleInput = input.render
		val addPageButton =
			button(
				"add page",
				*.onclick := { (event: dom.Event) ⇒
					Ajaxer[MainAPI].addPage(Page(pageTitleInput.value, Nil)).call()
					renderPages
				}
			)
		menuContainer.appendChild(
			div(
				pageTitleInput,
				br,
				addPageButton,
				pagesElem
			).render
		)
		renderPages
		dom.document.body.appendChild(
			div(
				menuContainer,
				messagesContainer
			).render
		)
	}

	val pagesElem = ul().render
	def renderPages: Unit = {
		Ajaxer[MainAPI].getPages().call().foreach {
			pages ⇒ fillPagesUL(pagesElem, pages)
		}
	}

	def fillPagesUL(pagesElem: HTMLElement, pages: Seq[Page]): Unit = {
		clearComponent(pagesElem)
		pagesElem.appendChild(
			ul(
				pages.map(page ⇒
					li(
						button(
							page.title,
							*.onclick := { (event: dom.Event) ⇒ {
								clearComponent(messagesContainer)
								messagesContainer.appendChild(PageClient.page2Element(page))
							}
							}
						),
						button(
							"-",
							*.onclick := { (event: dom.Event) ⇒ {
								clearComponent(messagesContainer)
								Ajaxer[MainAPI].deletePage(page).call().foreach {
									isDeleted ⇒ if (isDeleted) renderPages
								}
							}
							}
						)
//						,
//						button(
//							"Kanban",
//							*.onclick := { (event: dom.Event) ⇒ {
//								clearComponent(messagesContainer)
//								PageClient.pageKanban(messagesContainer, page)
//							}
//							}
//						)
					)
				)
			).render
		)
	}

	def clearComponent(component: HTMLElement): Unit = {
		while (component.firstChild != null) {
			component.removeChild(component.firstChild)
		}
	}

}
