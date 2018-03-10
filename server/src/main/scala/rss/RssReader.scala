package rss

import java.net.URL
import java.text.SimpleDateFormat
import java.util.{Date, Locale}

import shared.rss._

import scala.util.{Failure, Success, Try}
import scala.xml._


abstract class Reader {

	def extract(xml: Elem): Seq[RssFeed]

	def print(feed: RssFeed) {
		println(feed.latest)
	}
}

object AtomReader extends Reader {

	val dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH)

	private def parseAtomDate(date: String, formatter: SimpleDateFormat): Date = {
		val newDate = date.reverse.replaceFirst(":", "").reverse
		return formatter.parse(newDate)
	}

	private def getHtmlLink(node: NodeSeq) = {
		node
			.filter(n => (n \ "@type").text == "text/html")
			.map(n => (n \ "@href").text).head
	}

	def extract(xml: Elem): Seq[RssFeed] = {
		for (feed <- xml \\ "feed") yield {
			val items = for (item <- (feed \\ "entry")) yield {
				RssItem(
					(item \\ "title").text,
					getHtmlLink((item \\ "link")),
					(item \\ "summary").text,
					parseAtomDate((item \\ "published").text, dateFormatter).toString,
					(item \\ "id").text
				)
			}
			AtomRssFeed(
				(feed \ "title").text,
				getHtmlLink((feed \ "link")),
				(feed \ "subtitle ").text,
				items.take(8))
		}
	}

}

object XmlReader extends Reader {

	val dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);

	def extract(xml: Elem): Seq[RssFeed] = {

		for (channel <- xml \\ "channel") yield {
			val items = for (item <- (channel \\ "item")) yield {
				RssItem(
					(item \\ "title").text,
					(item \\ "link").text,
					(item \\ "description").text,
					dateFormatter.parse((item \\ "pubDate").text).toString,
					(item \\ "guid").text
				)
			}
			XmlRssFeed(
				(channel \ "title").text,
				(channel \ "link").text,
				(channel \ "description").text,
				(channel \ "language").text,
				items.take(8))
		}
	}

}


object RssReader {

	def read(url: URL): Seq[RssFeed] = {
		Try(url.openConnection.getInputStream) match {
			case Success(u) => {
				val xml = XML.load(u)
				if ((xml \\ "channel").length == 0) AtomReader.extract(xml) else XmlReader.extract(xml)
			}
			case Failure(_) =>
				println(s"error on load rss xml from $url")
				Nil
		}
	}

}