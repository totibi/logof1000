package com.memories.logof1000

import com.memories.logof1000.cms.page.PageController
import com.memories.logof1000.shared.cms.page.Page
import org.scalajs.dom


import scalatags.JsDom.short._
import scala.scalajs.js.annotation.JSExport

object ScalaJSExample {

  def main(args: Array[String]): Unit = {
    dom.document.body.appendChild(
      button(
				*.onclick := PageController.addPageRequest(Page("kek2018")){
					xhr ⇒ {
						val pageFromServer = upickle.default.read[Page](xhr.responseText)
						renderPage(pageFromServer)
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
