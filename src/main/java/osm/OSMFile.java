package osm;

import java.util.Iterator;

import osm.primitive.Primitive;
import osm.primitive.PrimitiveTypeEnum;
import osm.primitive.node.Node;
import osm.primitive.relation.Member;
import osm.primitive.relation.Relation;
import osm.primitive.way.Way;
import util.IDGenerator;

/**
 * @author Ian Dees
 * 
 */
public class OSMFile {

    private Node[] nodes = new Node[50000];
    private Way[] ways = new Way[10000];
    private Relation[] relations = new Relation[5000];
    private int nodeIdOffset;
    private int wayIdOffset;
    private int relationIdOffset;
    private int relationCount;
    private int wayCount;
    private int nodeCount;
    
    public OSMFile() {
        nodeIdOffset = IDGenerator.currentNodeID();
        wayIdOffset = IDGenerator.currentWayID();
        relationIdOffset = IDGenerator.currentRelationID();
    }

    public void addNode(Node n) {
        if (n.getID() == 0) {
            n.setID(IDGenerator.nextNodeID());
        }

        nodeCount++;
        addPrimitive(nodes, n, nodeIdOffset);
    }

    private <M extends Primitive> void addPrimitive(M[] list, M n, int offset) {
        int index = (Math.abs(n.getID())) + offset;
        try {
            if (list[index] == null) {
                list[index] = n;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw e;
        }
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
        addPrimitive(ways, w, wayIdOffset);
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
        addPrimitive(relations, r, relationIdOffset);
    }
    
    class PrimitiveIterator<P extends Primitive> implements Iterator<P> {
        private int i = 0;
        private P[] prims;

        public PrimitiveIterator(P[] primitives) {
            prims = primitives;
        }

        @Override
        public boolean hasNext() {
            return (i < prims.length) && (prims[i] != null);
        }

        @Override
        public P next() {
            return prims[i++];
        }

        @Override
        public void remove() {
            return;
        }
        
    }

    public Iterator<Node> getNodeIterator() {
        return new PrimitiveIterator<Node>(nodes);
    }

    public Iterator<Way> getWayIterator() {
        return new PrimitiveIterator<Way>(ways);
    }

    public Iterator<Relation> getRelationIterator() {
        return new PrimitiveIterator<Relation>(relations);
    }

    public int getNodeCount() {
        return nodeCount;
    }

}
