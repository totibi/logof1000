package client.cms.view.graph
import org.scalajs.dom
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}
import scalatags.JsDom.short._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object GraphView {
	def renderGraph(container: HTMLElement): Unit = {
		container.clearFromChildren()
		val popupContents = div().render
		popupContents.innerHTML =
			"""
				|<div id="node-popUp">
				|  <span id="node-operation">node</span> <br>
				|  <table style="margin:auto;">
				|    <tr>
				|      <td>id</td><td><input id="node-id" value="new value" /></td>
				|    </tr>
				|    <tr>
				|      <td>label</td><td><input id="node-label" value="new value" /></td>
				|    </tr>
				|  </table>
				|  <input type="button" value="save" id="node-saveButton" />
				|  <input type="button" value="cancel" id="node-cancelButton" />
				|</div>
				|
				|<div id="edge-popUp">
				|  <span id="edge-operation">edge</span> <br>
				|  <table style="margin:auto;">
				|    <tr>
				|      <td>label</td><td><input id="edge-label" value="new value" /></td>
				|    </tr></table>
				|  <input type="button" value="save" id="edge-saveButton" />
				|  <input type="button" value="cancel" id="edge-cancelButton" />
				|</div>
			""".stripMargin
		container.parentElement.appendChild(popupContents)
		container.appendChildren(
			script(
				s"""
					|   // create an array with nodes
					|    var nodes = new vis.DataSet([
					|        {id: 1, label: 'Node 1'},
					|        {id: 2, label: 'Node 2'},
					|        {id: 3, label: 'Node 3'},
					|        {id: 4, label: 'Node 4'},
					|        {id: 5, label: 'Node 5'}
					|    ]);
					|
					|    // create an array with edges
					|    var edges = new vis.DataSet([
					|        {from: 1, to: 3},
					|        {from: 1, to: 2},
					|        {from: 2, to: 4},
					|        {from: 2, to: 5}
					|    ]);
					|    // create a network
					|    var container = document.getElementById('${container.id}');
					|    // provide the data in the vis format
					|    var data = {
					|        nodes: nodes,
					|        edges: edges
					|    };
					|      var options = {
					|          manipulation: {
					|          addNode: function (data, callback) {
					|            // filling in the popup DOM elements
					|            document.getElementById('node-operation').innerHTML = "Add Node";
					|            editNode(data, callback);
					|          },
					|          editNode: function (data, callback) {
					|            // filling in the popup DOM elements
					|            document.getElementById('node-operation').innerHTML = "Edit Node";
					|            editNode(data, callback);
					|          },
					|          addEdge: function (data, callback) {
					|            if (data.from == data.to) {
					|              var r = confirm("Do you want to connect the node to itself?");
					|              if (r != true) {
					|                callback(null);
					|                return;
					|              }
					|            }
					|            document.getElementById('edge-operation').innerHTML = "Add Edge";
					|            editEdgeWithoutDrag(data, callback);
					|          },
					|          editEdge: {
					|            editWithoutDrag: function(data, callback) {
					|              document.getElementById('edge-operation').innerHTML = "Edit Edge";
					|              editEdgeWithoutDrag(data,callback);
					|            }
					|          }
					|        }
					|      };
					|
					|    // initialize your network!
					|    var network = new vis.Network(container, data, options);
					|
				""".stripMargin
			).render
		)
	}

	@JSExportTopLevel("editNode")
	def editNode(data: js.Dynamic, callback: js.Function1[js.Dynamic, _]) {
		dom.document.getElementById("node-label").asInstanceOf[HTMLInputElement].value = data.label.toString
		dom.document.getElementById("node-saveButton").asInstanceOf[HTMLElement].onclick ={ (event: dom.Event) ⇒ saveNodeData(data, callback);}
		dom.document.getElementById("node-cancelButton").asInstanceOf[HTMLElement].onclick = { (event: dom.Event) ⇒ cancelNodeEdit(callback);}
		dom.document.getElementById("node-popUp").asInstanceOf[HTMLElement].style.display = "block"
	}

	// Callback passed as parameter is ignored
	def clearNodePopUp() {
		dom.document.getElementById("node-saveButton").asInstanceOf[HTMLElement].onclick = null
		dom.document.getElementById("node-cancelButton").asInstanceOf[HTMLElement].onclick = null
		dom.document.getElementById("node-popUp").asInstanceOf[HTMLElement].style.display = "none"
	}

	def cancelNodeEdit(callback: js.Function1[js.Dynamic, _]) {
		clearNodePopUp()
		callback(null)
	}

	def saveNodeData(data: js.Dynamic, callback: js.Function1[js.Dynamic, _]) {
		data.label = dom.document.getElementById("node-label").asInstanceOf[HTMLInputElement].value
		clearNodePopUp()
		callback(data)
	}

	@JSExportTopLevel("editEdgeWithoutDrag")
	def editEdgeWithoutDrag(data: js.Dynamic, callback: js.Function1[js.Dynamic, _]) {
		// filling in the popup DOM elements
		dom.document.getElementById("edge-label").asInstanceOf[HTMLInputElement].value = data.label.toString
		dom.document.getElementById("edge-saveButton").asInstanceOf[HTMLElement].onclick = { (event: dom.Event) ⇒ saveEdgeData(data, callback);}
		dom.document.getElementById("edge-cancelButton").asInstanceOf[HTMLElement].onclick = { (event: dom.Event) ⇒ cancelEdgeEdit(callback);}
		dom.document.getElementById("edge-popUp").asInstanceOf[HTMLElement].style.display = "block";
	}

	def cancelEdgeEdit(callback: js.Function1[js.Dynamic, _]): Unit = {
		clearEdgePopUp()
		callback(null)
	}

	def clearEdgePopUp(): Unit = {
		dom.document.getElementById("edge-saveButton").asInstanceOf[HTMLElement].onclick = null
		dom.document.getElementById("edge-cancelButton").asInstanceOf[HTMLElement].onclick = null
		dom.document.getElementById("edge-popUp").asInstanceOf[HTMLElement].style.display = "none"
	}

	def saveEdgeData(data: js.Dynamic, callback: js.Function1[js.Dynamic, _]) = {
		if (data.to.isInstanceOf[js.Object]) data.to = data.to.id
		if (data.from.isInstanceOf[js.Object]) data.from = data.from.id
		data.label = dom.document.getElementById("edge-label").asInstanceOf[HTMLInputElement].value
		clearEdgePopUp()
		callback(data)
	}


}
