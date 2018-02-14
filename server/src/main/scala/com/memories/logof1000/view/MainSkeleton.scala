package com.memories.logof1000.view
import scalatags.Text.all._
import scalatags.Text.tags2.title

object MainSkeleton {
	val onLoadScript =
		"com.memories.logof1000.Client().main()"
	val skeleton =
		"<!DOCTYPE html>" +
		html(
			head(
				title("Log Of 1000"),
				meta(httpEquiv:="Content-Type", content:="text/html; charset=UTF-8"),
				script(`type`:="text/javascript", src:="/client-fastopt.js") // позволяет использовать сгенерированные scala.js скрипты
			),
			body(
				onload := onLoadScript,
			ul(
				for (file ← Option(new java.io.File(".").listFiles()).toSeq.flatten)
					li(file.getName)
			),
			p("Hello")
			)
		).render

}