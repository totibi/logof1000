package client.rss

import autowire._
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLElement
import shared.MainAPI

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scalatags.JsDom.short._

object RssClient {
	def fetchFeedsButton: HTMLElement = {
		val titlesBlock: HTMLElement = div(
			*.float := "left",
			"News"
		).render
		val urlInput = input.render
		val addMessageButton =
			button(
				"get feeds",
				*.onclick := { (event: dom.Event) ⇒
					client.Ajaxer[MainAPI].fetchFeeds(urlInput.value).call().foreach { feeds ⇒
							feeds.foreach(feed ⇒ {
								titlesBlock.appendChild(
									div(
										hr,
										feed.title,
										feed.items.map(feedItem ⇒ div (
											a(
												*.href := s"${feedItem.link}",
												p(b({feedItem.title}))
											),
											span(feedItem.desc),
											p(b(feedItem.date)),
											hr
										))
									).render
								)
							})
					}
				}
			).render
		div(
			*.float := "left",
			*.width := "100%",
			*.height := "100%",
			urlInput, addMessageButton, titlesBlock
		).render
	}

	def skeleton: HTMLElement = {
		div(
			fetchFeedsButton
		).render
	}
}
