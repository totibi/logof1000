package shared.cms.Graph

trait GraphAPI {
	def saveGraph(graphToSave: Graph): Graph
	def getLastGraph(boolean: Boolean = true): Option[Graph]
}
