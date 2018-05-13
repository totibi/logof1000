package client

import client.cms.view.MainView
import org.scalajs.dom
import upickle.default

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

object Ajaxer extends autowire.Client[String, upickle.default.Reader, upickle.default.Writer] {
	override def doCall(req: Request): Future[String] = {
		dom.ext.Ajax.post(
			url = "/api/" + req.path.mkString("/"),
			data = upickle.default.write(req.args)
		).map(response ⇒ {
			response.responseText
		})
	}

	override def read[Result](p: String)(implicit evidence$1: default.Reader[Result]): Result = upickle.default.read[Result](p)

	override def write[Result](r: Result)(implicit evidence$2: default.Writer[Result]): String =  upickle.default.write(r)
}

@JSExportTopLevel("Client")
object Client {

	@JSExport
	def main(): Unit = {
		dom.document.body.appendChild(
			MainView.openMainView
		)
	}

}
