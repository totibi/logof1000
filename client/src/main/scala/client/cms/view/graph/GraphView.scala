package client.cms.view.graph
import org.scalajs.dom.raw.HTMLElement
import scalatags.JsDom.short._

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
					|            editNode(data, clearNodePopUp, callback);
					|          },
					|          editNode: function (data, callback) {
					|            // filling in the popup DOM elements
					|            document.getElementById('node-operation').innerHTML = "Edit Node";
					|            editNode(data, cancelNodeEdit, callback);
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
					|    function editNode(data, cancelAction, callback) {
					|      document.getElementById('node-label').value = data.label;
					|      document.getElementById('node-saveButton').onclick = saveNodeData.bind(this, data, callback);
					|      document.getElementById('node-cancelButton').onclick = cancelAction.bind(this, callback);
					|      document.getElementById('node-popUp').style.display = 'block';
					|    }
					|
 					|    // Callback passed as parameter is ignored
					|    function clearNodePopUp() {
					|      document.getElementById('node-saveButton').onclick = null;
					|      document.getElementById('node-cancelButton').onclick = null;
					|      document.getElementById('node-popUp').style.display = 'none';
					|    }
					|
 					|    function cancelNodeEdit(callback) {
					|      clearNodePopUp();
					|      callback(null);
					|    }
					|
 					|    function saveNodeData(data, callback) {
					|      data.label = document.getElementById('node-label').value;
					|      clearNodePopUp();
					|      callback(data);
					|    }
					|        function editEdgeWithoutDrag(data, callback) {
					|      // filling in the popup DOM elements
					|      document.getElementById('edge-label').value = data.label;
					|      document.getElementById('edge-saveButton').onclick = saveEdgeData.bind(this, data, callback);
					|      document.getElementById('edge-cancelButton').onclick = cancelEdgeEdit.bind(this,callback);
					|      document.getElementById('edge-popUp').style.display = 'block';
					|    }
					|
 					|    function clearEdgePopUp() {
					|      document.getElementById('edge-saveButton').onclick = null;
					|      document.getElementById('edge-cancelButton').onclick = null;
					|      document.getElementById('edge-popUp').style.display = 'none';
					|    }
					|
 					|    function cancelEdgeEdit(callback) {
					|      clearEdgePopUp();
					|      callback(null);
					|    }
					|
 					|    function saveEdgeData(data, callback) {
					|      if (typeof data.to === 'object')
					|        data.to = data.to.id
					|      if (typeof data.from === 'object')
					|        data.from = data.from.id
					|      data.label = document.getElementById('edge-label').value;
					|      clearEdgePopUp();
					|      callback(data);
					|    }
				""".stripMargin
			).render
		)
	}
}
