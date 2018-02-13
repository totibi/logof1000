package com.memories.logof1000.shared.cms.page

import upickle.default.{ReadWriter => RW, macroRW}

case class Page(title: String) {

}

object Page{
	implicit def rw: RW[Page] = macroRW
}
