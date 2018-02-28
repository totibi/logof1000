package client.rss

import autowire._
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLElement
import shared.MainAPI

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scalatags.JsDom.short._

object RssClient {
	def fetchFeedsButton: HTMLElement = {
		val urlInput = input.render
		val addMessageButton =
			button(
				"get feeds",
				*.onclick := { (event: dom.Event) ⇒
					client.Ajaxer[MainAPI].fetchFeeds(urlInput.value).call().foreach { feeds ⇒
							feeds.foreach(feed ⇒ titlesBlock.appendChild(p(feed.title).render))
					}
				}
			).render
		div(
			*.float := "left",
			*.width := "100%",
			*.height := "100%",
			urlInput, addMessageButton
		).render
	}

	def titlesBlock: HTMLElement = div(*.float := "right").render

	def skeleton: HTMLElement = {
		div(
			fetchFeedsButton,
			titlesBlock
		).render
	}
}
