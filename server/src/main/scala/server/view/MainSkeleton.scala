package server.view

import scalatags.Text.all._
import scalatags.Text.tags2.title

object MainSkeleton {
	val onLoadScript =
		"Client.main()"
	val skeleton =
		"<!DOCTYPE html>" +
		html(
			head(
				title("Log Of 1000"),
				meta(httpEquiv:="Content-Type", content:="text/html; charset=UTF-8"),
				script(`type`:="text/javascript", src:="/assets/client-fastopt.js"), // позволяет использовать сгенерированные scala.js скрипты
				script(`type`:="text/javascript", src:="/assets/tinymce/tinymce.min.js")
			),
			body(
				onload := onLoadScript,
			"Hello"
			)
		).render

}