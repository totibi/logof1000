package shared.rss

import java.net.URL

import upickle.default.{macroRW, ReadWriter ⇒ RW}


case class RssUrl(url:URL) {
	override def toString = "RSS: " + url.toString
}

sealed trait RssFeed {
	val link:String
	val title:String
	val desc:String
	val items:Seq[RssItem]
	override def toString = title + "\n" + desc + "\n**"

	def latest = items sortWith ((a, b) => a.date.compareTo(b.date) > 0) head
}
object RssFeed{implicit def rw: RW[RssFeed] = macroRW}


case class AtomRssFeed(title:String, link:String, desc:String, items:Seq[RssItem]) extends RssFeed
object AtomRssFeed{implicit def rw: RW[AtomRssFeed] = macroRW}
case class XmlRssFeed(title:String, link:String, desc:String, language:String, items:Seq[RssItem]) extends RssFeed
object XmlRssFeed{implicit def rw: RW[XmlRssFeed] = macroRW}

case class RssItem(title:String, link:String, desc:String, date: String, guid:String) {
	override def toString = date + " " + title
}

object RssItem{implicit def rw: RW[RssItem] = macroRW}
