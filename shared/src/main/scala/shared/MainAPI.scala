package shared

import shared.cms.Graph.GraphAPI
import shared.cms.page.PageAPI
import shared.rss.RssAPI

trait MainAPI extends PageAPI with RssAPI with GraphAPI
