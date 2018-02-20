package client.cms.page

import org.scalajs.dom.raw.HTMLElement
import shared.cms.page.Page

import scalatags.JsDom.short._

object PageClient {
	def page2Elemnent(page: Page): HTMLElement = {
		div(
			h1("Hello this is " + page.title)
		).render
	}
}
