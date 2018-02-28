package server

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives
import com.typesafe.config.ConfigFactory
import server.view.MainSkeleton
import shared.MainAPI
import upickle.default

import scala.concurrent.ExecutionContext.Implicits.global

object ServerRouter extends autowire.Server[String, upickle.default.Reader, upickle.default.Writer] {
	override def read[Result](p: String)(implicit evidence$1: default.Reader[Result]): Result = upickle.default.read[Result](p)

	override def write[Result](r: Result)(implicit evidence$2: default.Writer[Result]): String = upickle.default.write(r)
}

object Server extends Directives with ServerController {

	def main(args: Array[String]) {

		val config = ConfigFactory.load()
		val interface = config.getString("http.interface")
		val port = config.getInt("http.port")


		Http().bindAndHandle(route, interface, port)

		println(s"Server online at http://$interface:$port")
	}

	val route = {
		pathSingleSlash {
			get {
				complete {
					HttpEntity(
						ContentTypes.`text/html(UTF-8)`,
						MainSkeleton.skeleton
					)
				}
			}
		} ~
		getFromResourceDirectory("") ~
		path("api" / Segments) { segment ⇒
			post {
				 entity(as[String]) { entity ⇒
						complete {
							ServerRouter.route[MainAPI](Server)(autowire.Core.Request(segment, upickle.default.read[Map[String, String]](entity)))
						}
				}
			}
		}
	}
}
