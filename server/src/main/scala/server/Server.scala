package server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import server.cms.{PageContainer, PageContainerInMemory}
import server.view.MainSkeleton
import shared.cms.page.{Page, PageApi}
import upickle.default

import scala.concurrent.ExecutionContext.Implicits.global

object ServerRouter extends autowire.Server[String, upickle.default.Reader, upickle.default.Writer] {
	override def read[Result](p: String)(implicit evidence$1: default.Reader[Result]): Result = upickle.default.read[Result](p)

	override def write[Result](r: Result)(implicit evidence$2: default.Writer[Result]): String = upickle.default.write(r)
}

trait PageApiImp extends PageApi {
	val pageContainer: PageContainer = PageContainerInMemory

	override def addPage(newPage: Page): Page = {
		pageContainer.addPage(newPage)
		newPage
	}

	override def getPages(emptyDontWork: Boolean = true): Seq[Page] = {
		pageContainer.getPages
	}

}

object Server extends Directives with PageApiImp {

	def main(args: Array[String]) {
		implicit val system = ActorSystem("server-system")
		implicit val materializer = ActorMaterializer()

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
		path("ajax" / Segments) { segment ⇒
			post {
				 entity(as[String]) { entity ⇒
						complete {
							ServerRouter.route[PageApi](Server)(autowire.Core.Request(segment, upickle.default.read[Map[String, String]](entity)))
						}
				}
			}
		}
	}
}
