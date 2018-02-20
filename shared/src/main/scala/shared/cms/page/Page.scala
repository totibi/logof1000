package shared.cms.page

import upickle.default.{macroRW, ReadWriter ⇒ RW}

case class Page(title: String)

object Page{
	implicit def rw: RW[Page] = macroRW
}
