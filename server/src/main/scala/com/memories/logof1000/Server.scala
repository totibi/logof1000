package com.memories.logof1000

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import com.memories.logof1000.shared.cms.page.{Page, PageApi}
import com.memories.logof1000.view.MainSkeleton
import com.typesafe.config.ConfigFactory
import upickle.default

import scala.concurrent.ExecutionContext.Implicits.global

object ServerRouter extends autowire.Server[String, upickle.default.Reader, upickle.default.Writer]{
	override def read[Result](p: String)(implicit evidence$1: default.Reader[Result]): Result = upickle.default.read[Result](p)

	override def write[Result](r: Result)(implicit evidence$2: default.Writer[Result]): String = upickle.default.write(r)
}

object Server extends Directives with PageApi{

	def main(args: Array[String]) {
		implicit val system = ActorSystem("server-system")
		implicit val materializer = ActorMaterializer()

		val config = ConfigFactory.load()
		val interface = config.getString("http.interface")
		val port = config.getInt("http.port")


		Http().bindAndHandle(route, interface, port)

		println(s"Server online at http://$interface:$port")
	}


	override def getPage(kek: String): Page = {
		println(kek)
		Page(kek)
	}

	val route = {
   get{
		 pathSingleSlash{
			complete{
				HttpEntity(
					ContentTypes.`text/html(UTF-8)`,
					MainSkeleton.skeleton
				)
			}
		 } ~
		 getFromResourceDirectory("")
	 } ~
    post{
			path("ajax" / Segments){
				segment ⇒
				entity(as[String]){
					entity ⇒ complete{
						ServerRouter.route[PageApi](Server)(autowire.Core.Request(segment, upickle.default.read[Map[String, String]](entity)))
					}
				}
			}
		}
  }
}
