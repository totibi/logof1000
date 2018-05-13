package client.cms.view.page

import org.scalajs.dom.raw.HTMLElement
import shared.cms.page.Page

// TODO look for better they adding api to implicit class or should it be another impl class on one biggest, DI
// implicit page specific methods for html elements
trait WithRichElementForPage {

	implicit class RichHTMLElementForPage(val currentElem: HTMLElement) {

		// TODO fluent decorator
		// Adding page html elements to current html element
		def appendPage(page: Page): HTMLElement = {
			PageView.buildPageContent(currentElem, page)
			currentElem
		}

	}

}
