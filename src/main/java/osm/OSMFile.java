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

    private Node[] nodes = new Node[30000];
    private Way[] ways = new Way[10000];
    private Relation[] relations = new Relation[5000];
    private int idOffset;
    private int relationCount;
    private int wayCount;
    private int nodeCount;
    
    public OSMFile(int offset) {
        idOffset = offset;
    }
    
    public OSMFile() {
        this(0);
    }

    public void addNode(Node n) {
        if (n.getID() == 0) {
            n.setID(IDGenerator.nextNodeID());
        }

        nodeCount++;
        addPrimitive(nodes, n);
    }

    private <M extends Primitive> void addPrimitive(M[] list, M n) {
        int index = (Math.abs(n.getID()) - 1) - idOffset;
        try {
            if (list[index] != null) {
                list[index] = n;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
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

    public Iterator<Node> getNodeIterator() {
        return new Iterator<Node>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return (i < nodes.length) && (nodes[i + 1] != null);
            }

            @Override
            public Node next() {
                return nodes[++i];
            }

            @Override
            public void remove() {
                return;
            }

        };
    }

    public Iterator<Way> getWayIterator() {
        return new Iterator<Way>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return (i < ways.length) && (ways[i + 1] != null);
            }

            @Override
            public Way next() {
                return ways[++i];
            }

            @Override
            public void remove() {
                return;
            }

        };
    }

    public Iterator<Relation> getRelationIterator() {
        return new Iterator<Relation>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return (i < relations.length) && (relations[i + 1] != null);
            }

            @Override
            public Relation next() {
                return relations[++i];
            }

            @Override
            public void remove() {
                return;
            }

        };
    }

    public int getNodeCount() {
        return nodeCount;
    }

}
