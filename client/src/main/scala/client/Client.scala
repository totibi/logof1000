package client

import autowire._
import client.cms.page.PageClient
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLElement
import shared.cms.page.{Page, PageApi}
import upickle.default

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scalatags.JsDom.short._

object Ajaxer extends autowire.Client[String, upickle.default.Reader, upickle.default.Writer] {
	override def doCall(req: Request): Future[String] = {
		dom.ext.Ajax.post(
			url = "/ajax/" + req.path.mkString("/"),
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

	val menuContainer: HTMLElement = div(*.float := "left").render
	// Отоброжает содержимое выбранной страницы
	val messagesContainer: HTMLElement = div(*.float := "left").render

	@JSExport
	def main(): Unit = {
		val pageTitleInput = input.render
		val addPageButton =
			button(
				"add page",
				*.onclick := { (event: dom.Event) ⇒
					Ajaxer[PageApi].addPage(Page(pageTitleInput.value)).call()
				}
			)
		val pagesElem = ul().render
		val getPagesButton =
			button(
				"get pages",
				*.onclick := { (event: dom.Event) ⇒
					Ajaxer[PageApi].getPages().call().foreach {
						pages ⇒ fillPagesUL(pagesElem, pages)
					}
				}
			).render
		menuContainer.appendChild(
			div(
				pageTitleInput,
				br,
				addPageButton,
				getPagesButton,
				pagesElem
			).render
		)
		dom.document.body.appendChild(
			div(
				menuContainer,
				messagesContainer
			).render
		)
	}

	@JSExport
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
						)
					)
				)
			).render
		)
	}

	def clearComponent(component: HTMLElement): Unit = {
		while (component.firstChild != null){
			component.removeChild(component.firstChild)
		}
	}

}
