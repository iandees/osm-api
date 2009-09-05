package osm;

import osm.primitive.Primitive;
import osm.primitive.PrimitiveTypeEnum;
import osm.primitive.node.Node;
import osm.primitive.relation.Member;
import osm.primitive.relation.Relation;
import osm.primitive.way.Way;
import util.IDGenerator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Ian Dees
 * 
 */
public class OSMFile {

    private List<Node> nodes = new LinkedList<Node>();
    private List<Way> ways = new LinkedList<Way>();
    private List<Relation> relations = new LinkedList<Relation>();
    private int relationCount;
    private int wayCount;
    private int nodeCount;
    
    public void addNode(Node n) {
        if (n.getID() == 0) {
            n.setID(IDGenerator.nextNodeID());
        }

        nodeCount++;
        addPrimitive(nodes, n);
    }

    private <M extends Primitive> void addPrimitive(List<M> list, M n) {
        list.add(n);
    }

    public void addWay(Way w) {
        if (w.getID() > -1) {
            w.setID(IDGenerator.nextWayID());
        }

        Iterator<Node> nodeIterator = w.getNodeIterator();
        while (nodeIterator.hasNext()) {
            Node node = (Node) nodeIterator.next();
            addNode(node);
        }

        wayCount++;
        addPrimitive(ways, w);
    }

    public void addRelation(Relation r) {
        if (r.getID() > -1) {
            r.setID(IDGenerator.nextRelationID());
        }

        Iterator<Member> memberIterator = r.getMemberIterator();
        while (memberIterator.hasNext()) {
            Member member = (Member) memberIterator.next();

            Primitive primitive = member.getMember();
            PrimitiveTypeEnum type = primitive.getType();

            if (PrimitiveTypeEnum.node.equals(type)) {
                addNode((Node) primitive);
            } else if (PrimitiveTypeEnum.way.equals(type)) {
                addWay((Way) primitive);
            } else if (PrimitiveTypeEnum.relation.equals(type)) {
                addRelation((Relation) primitive);
            }
        }

        relationCount++;
        addPrimitive(relations, r);
    }
    
    public int getChangeCount() {
        return nodeCount + wayCount + relationCount;
    }

    public Iterator<Node> getNodeIterator() {
        return nodes.iterator();
    }

    public Iterator<Way> getWayIterator() {
        return ways.iterator();
    }

    public Iterator<Relation> getRelationIterator() {
        return relations.iterator();
    }

    public int getNodeCount() {
        return nodeCount;
    }

}
