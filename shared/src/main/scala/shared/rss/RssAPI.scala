package shared.rss

trait RssAPI {
	def fetchFeeds(url: String): Seq[RssFeed]
}
