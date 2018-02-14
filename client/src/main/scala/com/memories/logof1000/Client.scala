package com.memories.logof1000

import autowire._
import com.memories.logof1000.shared.cms.page.{Page, PageApi}
import org.scalajs.dom
import upickle.default

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.short._

object Ajaxer extends autowire.Client[String, upickle.default.Reader, upickle.default.Writer]{
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

@JSExport
object Client {

	@JSExport
  def main(args: Array[String]): Unit = {
    dom.document.body.appendChild(
      button(
				*.onclick := { (event: dom.Event) ⇒
					Ajaxer[PageApi].getPage("wtf??!").call()
						.foreach { page ⇒
							renderPage(page)
						}
				},
				p("kekme")
      ).render
    )
  }

  def renderPage(pageToRender: Page): Unit = {
    dom.document.body.appendChild(
      div(
        p(pageToRender.title)
      ).render
    )
  }

}
