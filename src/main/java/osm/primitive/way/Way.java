package osm.primitive.way;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import osm.primitive.Primitive;
import osm.primitive.PrimitiveTypeEnum;
import osm.primitive.node.Node;
import util.IDGenerator;

/**
 * @author Ian Dees
 *
 */
public class Way extends Primitive {

    private List<Node> nodeList = new LinkedList<Node>();
    
    public void addNode(Node node) {
        if (node.getID() == 0) {
            node.setID(IDGenerator.nextNodeID());
        }
        
        nodeList.add(node);
    }

    public Iterator<Node> getNodeIterator() {
        return nodeList.iterator();
    }

    public PrimitiveTypeEnum getType() {
        return PrimitiveTypeEnum.way;
    }

	public int nodeCount() {
		return nodeList.size();
	}

    public List<Node> getNodes() {
        return nodeList;
    }

}
