package shared

import shared.cms.page.PageAPI
import shared.rss.RssAPI

trait MainAPI extends PageAPI with RssAPI{

}
