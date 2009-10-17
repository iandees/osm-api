package osm;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import osm.parser.OSMSaxParser;
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
        if (w.getID() < 0) {
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

    /**
     * @param file
     * @return
     */
    public static OSMFile fromFile(File file) {
        OSMSaxParser osmHandler = new OSMSaxParser();
        
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(file, osmHandler);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return osmHandler.getOSMFile();
    }

    /**
     * @param id
     * @return
     */
    public Node findNodeById(int id) {
        // TODO Need a better data structure for id => primitive
        for (Node node : nodes) {
            if(node.getID() == id) {
                return node;
            }
        }
        return null;
    }

    /**
     * @param refId
     * @return
     */
    public Primitive findRelationById(int refId) {
        // TODO Need a better data structure for id => primitive
        for (Relation relation : relations) {
            if(relation.getID() == refId) {
                return relation;
            }
        }
        return null;
    }

    /**
     * @param refId
     * @return
     */
    public Primitive findWayById(int refId) {
        // TODO Need a better data structure for id => primitive
        for (Way way : ways) {
            if(way.getID() == refId) {
                return way;
            }
        }
        return null;
    }

    /**
     * @return
     */
    public int getWayCount() {
        return wayCount;
    }

    /**
     * @return
     */
    public int getRelationCount() {
        return relationCount;
    }

    /**
     * Read in a list of OSM files and return an aggregate of all of them together.
     * @param files The list of files to read.
     * @return The combined data from the list of OSM files.
     */
    public static OSMFile fromFiles(List<File> files) {
        if(files == null) {
            throw new IllegalArgumentException("Files cannot be null.");
        }
        
        OSMFile aggregate = new OSMFile();
        for (File file : files) {
            if(file.exists()) {
                OSMFile f = OSMFile.fromFile(file);
                aggregate.appendTo(f);
            }
        }
        return aggregate;
    }

    public void appendTo(OSMFile f) {
        if(f == null) {
            throw new IllegalArgumentException("File cannot be null.");
        }
        
        this.nodes.addAll(f.nodes);
        this.nodeCount += f.nodeCount;
        
        this.ways.addAll(f.ways);
        this.wayCount += f.wayCount;
        
        this.relations.addAll(f.relations);
        this.relationCount += f.relationCount;
    }

}
