package client.common

// TODO autowire cannot be places here :(

// if autowire client ajaxer are used
trait WithAjaxer{
	// used after ajax call() method
	implicit val executorQueue = scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

}